package com.dotop.pipe.auth.core.vo.auth;

import lombok.Data;

@Deprecated
@Data
public class AuthCas {

	public AuthCas() {
		super();
	}

	public AuthCas(String userId, String ticket, String type) {
		super();
		this.userId = userId;
		this.ticket = ticket;
		this.type = type;
	}

	private String userId;

	private String ticket;

	private String type;
}
