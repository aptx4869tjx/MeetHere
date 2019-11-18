package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.dao.UserDao;
import com.tjx.MeetHere.dao.UserLoginDao;
import com.tjx.MeetHere.dataObject.User;
import com.tjx.MeetHere.dataObject.UserLogin;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.UserService;
import com.tjx.MeetHere.service.model.UserModel;
import com.tjx.MeetHere.service.model.UserShiro;
import com.tjx.MeetHere.validator.ValidationResult;
import com.tjx.MeetHere.validator.ValidatorImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserLoginDao userLoginDao;
    @Autowired
    ValidatorImpl validator;

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
}
