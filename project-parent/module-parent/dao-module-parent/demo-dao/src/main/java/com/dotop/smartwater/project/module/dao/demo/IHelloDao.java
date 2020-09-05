package com.dotop.smartwater.project.module.dao.demo;

import org.springframework.dao.DataAccessException;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.demo.dto.HelloDto;
import com.dotop.smartwater.project.module.core.demo.vo.HelloVo;

public interface IHelloDao extends BaseDao<HelloDto, HelloVo> {

	HelloVo get(HelloDto helloDto) throws DataAccessException;

}
