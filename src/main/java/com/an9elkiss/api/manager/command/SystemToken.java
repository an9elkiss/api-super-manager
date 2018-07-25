package com.an9elkiss.api.manager.command;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.an9elkiss.commons.auth.model.User;
import com.an9elkiss.commons.constant.RedisKeyPrefix;
import com.an9elkiss.commons.util.JsonUtils;
import com.an9elkiss.commons.util.spring.RedisUtils;

@Component
public class SystemToken {

	private static final ThreadLocal<TokenCmd> THREAD_LOCAL = new ThreadLocal<TokenCmd>();
	private static TokenCmd token_cmd = null;

	private static SystemToken systemToken;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@PostConstruct 
    public void init() { 
		systemToken = this; 
		systemToken.redisUtils = this.redisUtils; 
    } 
	
	public static TokenCmd getTokenCmd(User user) {
		TokenCmd tokenCmd = THREAD_LOCAL.get();

		if (tokenCmd == null) {
			token_cmd = TokenCmd.random();
			token_cmd.setUserName(user.getName());
			token_cmd.setId(user.getId());
			systemToken.redisUtils.setString(RedisKeyPrefix.SESSION + token_cmd.getToken(),  JsonUtils.toString(token_cmd), 25l, TimeUnit.HOURS);
			THREAD_LOCAL.set(token_cmd);
			return token_cmd;
		} else {
			return tokenCmd;
		}
	}

	public static void setTokenCmd(TokenCmd tokenCmd) {
		THREAD_LOCAL.set(tokenCmd);
	}

	public static void cleanTokenCmd() {
		THREAD_LOCAL.set(token_cmd);
	}
}
