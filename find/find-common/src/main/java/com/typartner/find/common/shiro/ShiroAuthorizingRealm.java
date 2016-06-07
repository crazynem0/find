package com.typartner.find.common.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.typartner.find.common.constant.CommonConstants;
import com.typartner.find.common.model.SysMember;
import com.typartner.find.common.model.SysUser;


public class ShiroAuthorizingRealm extends AuthorizingRealm {

	

	/**
	 * 认证回调函数,登录时调用.
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		// TODO Auto-generated method stub
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String pwd=new String(token.getPassword());
		SysMember user = null;
		if(pwd!=null&&"".equals(pwd)){
			user = SysMember.dao.findByLoginName( token.getUsername());
		}else
			user = SysMember.dao.findByLoginNameAndPwd( token.getUsername(),new String(token.getPassword()));
		
		if (user == null) 
			throw new UnknownAccountException();// 没找到帐号

		ShiroPrincipal subject = new ShiroPrincipal(user);
		return new SimpleAuthenticationInfo(subject, pwd, getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}