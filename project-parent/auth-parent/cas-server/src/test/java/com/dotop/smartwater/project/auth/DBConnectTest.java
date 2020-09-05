package com.dotop.smartwater.project.auth;

import com.dotop.smartwater.project.auth.api.IAccountFactory;
import com.dotop.smartwater.project.auth.dao.IUserDao;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;

public  class DBConnectTest {
    @Resource
    IUserDao iUserDao;

    public static void main(String[] args) {
        UserDto user = new UserDto();
        user.setAccount("test");
        user.setPassword("test");
        user.setRoleid("test");
        user.setEnterpriseid("test");
        user.setStatus(0);
        user.setCreatetime(new Date());
        user.setCreateuser("test");









    }

}
