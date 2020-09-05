package com.dotop.pipe.auth.core.local;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;

import lombok.Data;

import java.util.Date;

@Data
public class LocalObj {

	private String token;

	private LoginCas loginCas;

	private Date curr;

}
