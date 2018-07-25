package com.an9elkiss.api.manager.command;

import java.util.UUID;

public class TokenCmd {

	private String token;
	private String userName;
	private Integer id;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static TokenCmd random() {
		TokenCmd tokenCmd = new TokenCmd();
		tokenCmd.setToken(UUID.randomUUID().toString());

		return tokenCmd;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
