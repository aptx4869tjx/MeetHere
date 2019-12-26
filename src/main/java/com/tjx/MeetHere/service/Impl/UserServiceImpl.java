package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.controller.viewObject.NewsVO;
import com.tjx.MeetHere.dao.NewsDao;
import com.tjx.MeetHere.dao.NewsImageDao;
import com.tjx.MeetHere.dao.UserDao;
import com.tjx.MeetHere.dao.UserLoginDao;
import com.tjx.MeetHere.dataObject.News;
import com.tjx.MeetHere.dataObject.NewsImage;
import com.tjx.MeetHere.dataObject.User;
import com.tjx.MeetHere.dataObject.UserLogin;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.PictureService;
import com.tjx.MeetHere.service.UserService;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.service.model.UserShiro;
import com.tjx.MeetHere.tool.ValidationResult;
import com.tjx.MeetHere.tool.ValidatorImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserLoginDao userLoginDao;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private NewsImageDao newsImageDao;
    @Autowired
    private NewsDao newsDao;

    @Override
    public UserModel getUserByUserId(Long userId) {
        User user = userDao.findByUserId(userId);
        UserLogin userLogin = userLoginDao.findByUserId(userId);
        return convertFromUserAndUserLogin(user, userLogin);
    }

    @Override
    public UserShiro getUserShiroByEmail(String email) {
        if (email == null) {
            return null;
        }
        UserShiro userShiro = new UserShiro();
        Long userId = userDao.selectUserIdByEmail(email);
        UserLogin userLogin = userLoginDao.findByUserId(userId);
        if (userLogin == null) {
            return null;
        }
        userShiro.setEmail(email);
        userShiro.setEncryptPassword(userLogin.getEncryptPassword());
        userShiro.setRole(userDao.selectRoleByUserId(userId));
        return userShiro;
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result = validator.validate(userModel);
        if (result.isHasError()) {
            throw new BusinessException(ErrorEm.USER_LOGIN_FAIL, result.getErrorMessage());
        }
        User user = getUserFromUserModel(userModel);
        if (userDao.existsByEmail(user.getEmail())) {
            throw new BusinessException(ErrorEm.EMAIL_HAS_REGISTED);
        }
        userDao.save(user);
        userModel.setUserId(user.getUserId());
        UserLogin userLogin = getUserLoginFromUserModel(userModel);
        userLoginDao.save(userLogin);
    }

    @Override
    public UserModel validateLogin(String email, String password) throws BusinessException {
        if (email == null || password == null || email.equals("") || password.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new BusinessException(ErrorEm.USER_NOT_EXIST);
        }
        UsernamePasswordToken token = new UsernamePasswordToken(email, password + "/" + MeetHereApplication.salt);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (Exception e) {
            throw new BusinessException(ErrorEm.USER_LOGIN_FAIL);
        }

        UserLogin userLogin = userLoginDao.findByUserId(user.getUserId());
        return convertFromUserAndUserLogin(user, userLogin);

    }

    @Override
    @Transactional
    public News publishNews(Long userId, String title, String content, String text, List<String> imgUrls) throws BusinessException {
        if (userId == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        if (title == null || title.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "标题不能为空");
        }
        if (text == null || text.equals("")) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR, "文字内容不能为空");
        }
        News news = new News();
        news.setUserId(userId);
        news.setTitle(title);
        news.setContent(content);
        news.setText(text);
        news.setTime(LocalDateTime.now());
        newsDao.save(news);
        if (imgUrls != null && imgUrls.size() > 0) {
            for (String imgUrl : imgUrls
            ) {
                newsImageDao.updateNewsImageByImgUrl(news.getNewsId(), imgUrl);
            }
        }
        return news;
    }

    @Override
    public String uploadNewsImage(MultipartFile uploadFile, String newName) {
        //TODO
        //运行在本机上的方法
        String imgUrl = pictureService.uploadPicture(uploadFile, newName);
        //运行在服务器上的方法
//        String imgUrl = pictureService.uploadFile(uploadFile, newName);

        NewsImage newsImage = new NewsImage();
        newsImage.setImageUrl(imgUrl);
        newsImage.setNewsId(0L);
        newsImageDao.save(newsImage);
        return imgUrl;
    }

    @Override
    public List<NewsVO> getNewsVO(Integer page) {
        Sort sort = new Sort(Sort.Direction.DESC, "newsId");
        PageRequest pageRequest = new PageRequest(page, 5, sort);
        List<News> newsList = newsDao.findAll(pageRequest).getContent();
        List<NewsVO> newsVOList = new ArrayList<>();
        for (News n : newsList
        ) {
            NewsVO newsVO = getNewsVOFromNews(n, true);
            newsVOList.add(newsVO);
        }
        return newsVOList;
    }

    @Override
    public NewsVO getNewsVO(Long newsId) {
        if (newsId == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        News news = newsDao.findByNewsId(newsId);
        NewsVO newsVO = getNewsVOFromNews(news, false);
        return newsVO;
    }

    @Override
    public void deleteNewsByNewsId(Long newsId) {
        if (newsId == null) {
            throw new BusinessException(ErrorEm.PARAMETER_VALIDATION_ERROR);
        }
        newsDao.deleteByNewsId(newsId);
        newsImageDao.deleteByNewsId(newsId);
    }

    private UserModel convertFromUserAndUserLogin(User user, UserLogin userLogin) {
        if (user == null || userLogin == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        userModel.setEncryptPassword(userLogin.getEncryptPassword());
        return userModel;

    }

    private User getUserFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userModel, user);
        return user;
    }

    private UserLogin getUserLoginFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserLogin userLogin = new UserLogin();
        BeanUtils.copyProperties(userModel, userLogin);
        return userLogin;
    }

    private NewsVO getNewsVOFromNews(News news, boolean simple) {
        NewsVO newsVO = new NewsVO();
        if (news == null) {
            return null;
        }
        if (simple) {
            //不需要获取用户名，但是需要图片的URL
            BeanUtils.copyProperties(news, newsVO);
            List<String> res = newsImageDao.getImageUrlsByNewsId(news.getNewsId());
            List<String> imgUrls;
            //只取前三张图片
            if (res.size() > 3) {
                imgUrls = res.subList(0, 3);
            } else {
                imgUrls = res;
            }
            newsVO.setImgUrls(imgUrls);
        } else {
            BeanUtils.copyProperties(news, newsVO);
            newsVO.setUsername(userDao.getUsernameByUserId(news.getUserId()));
        }
        return newsVO;
    }
}
