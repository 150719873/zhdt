package com.dotop.smartwater.project.module.core.auth.local;

import java.util.Date;

import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

import lombok.Data;

@Data
public class LocalObj {

	private UserVo user;

	private Date curr;
}
