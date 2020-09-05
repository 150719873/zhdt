package com.dotop.pipe.server.rest.service.utils;

import com.dotop.pipe.api.dao.test.ITestDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/util")
/**
 *
 */
public class EnterpriseUpdateController {

    @Autowired
    ITestDao iTestDao;
    @Autowired
    SqlSession sqlSession;

    @PostMapping(value = "/update-enterprise", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String update() throws FrameworkRuntimeException {
        this.exchangeEnterpriseId();
        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void exchangeEnterpriseId() {
        try {
            String dataBaseName = "pipe_2.6.2";
            Connection connection = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();
            List<String> tables = getTables(connection, dataBaseName);
            StringBuilder success = new StringBuilder();
            StringBuilder fail = new StringBuilder();
            Integer count = 0;
            List<Map<String, String>> pls_enterprise = iTestDao.getEidAndEnterpriseId("pls_enterprise");
            for (String tableName : tables) {
                for (Map<String, String> idMap : pls_enterprise) {
                    Map<String, String> map = new HashMap<>();
                    map.put("tableName", tableName);
                    try {
                        iTestDao.getEnterpriseId(map);
                    } catch (Exception e) {
                        fail.append(tableName);
                        fail.append(",");
                        break;
                    }
                    map.put("beforeEnterpriseId", idMap.get("enterprise_id"));
                    map.put("afterEnterpriseId", idMap.get("eid"));
                    Integer i = iTestDao.exchangeEnterpriseId(map);
                    count += i;
                    if (!success.toString().endsWith(tableName + ",")) {
                        success.append(tableName);
                        success.append(",");
                    }
                }
            }
            System.out.println("成功的表：" + success);
            System.out.println("修改数量：" + count);
            System.out.println("失败的表：" + fail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkRuntimeException(e.getMessage());
        }
    }

    private List<String> getTables(Connection connection, String catalog) throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(catalog, null, null, new String[]{"TABLE"});
        List<String> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getString("TABLE_NAME"));
        }
        return list;
    }
}
