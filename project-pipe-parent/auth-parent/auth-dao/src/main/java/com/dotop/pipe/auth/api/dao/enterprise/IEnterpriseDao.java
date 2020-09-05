package com.dotop.pipe.auth.api.dao.enterprise;

import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;

// 企业
public interface IEnterpriseDao {

    // 获取企业
    public EnterpriseVo get(@Param("enterpriseId") String enterpriseId, @Param("eid") String eid,
                            @Param("isDel") Integer isDel) throws DataAccessException;

    // 新增绑定企业
    public void add(@Param("enterpriseId") String enterpriseId, @Param("eid") String eid,
                    @Param("enterpriseName") String enterpriseName, @Param("protocols") List<String> protocols, @Param("curr") Date curr, @Param("userBy") String userBy,
                    @Param("isDel") Integer isDel) throws DataAccessException;

    // 修改企业默认地图和协议
    public int editMap(@Param("mapType") String mapType, @Param("protocols") List<String> protocols, @Param("enterpriseId") String enterpriseId,
                       @Param("userBy") String userBy, @Param("curr") Date curr);

}
