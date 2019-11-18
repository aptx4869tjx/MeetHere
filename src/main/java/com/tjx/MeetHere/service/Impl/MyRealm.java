package com.tjx.MeetHere.service.Impl;

import com.tjx.MeetHere.MeetHereApplication;
import com.tjx.MeetHere.error.BusinessException;
import com.tjx.MeetHere.error.ErrorEm;
import com.tjx.MeetHere.service.UserService;
import com.tjx.MeetHere.service.model.UserShiro;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    {
        //设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher hashMatcher = new HashedCredentialsMatcher();
        hashMatcher.setHashAlgorithmName("MD5");
        hashMatcher.setStoredCredentialsHexEncoded(true);
        hashMatcher.setHashIterations(1);
        this.setCredentialsMatcher(hashMatcher);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();
        if (email == null) {
            throw new AccountException("用户名为空");
        }
        UserShiro userShiro = userService.getUserShiroByEmail(email);
        if (userShiro == null) {
            throw new UnknownAccountException("此邮箱还未注册[" + email + "]");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(email, userShiro.getEncryptPassword(), "userLogin");
//        info.setCredentialsSalt(ByteSource.Util.bytes(MeetHereApplication.salt));//加盐
        return info;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
