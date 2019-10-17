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
import com.tjx.MeetHere.validator.ValidationResult;
import com.tjx.MeetHere.validator.ValidatorImpl;
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

        UserLogin userLogin = userLoginDao.findByUserId(user.getUserId());
        String encryptPassword = MeetHereApplication.getMD5(password);
        if (encryptPassword.equals(userLogin.getEncryptPassword())) {
            return convertFromUserAndUserLogin(user, userLogin);
        } else {
            throw new BusinessException(ErrorEm.USER_LOGIN_FAIL);
        }
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
