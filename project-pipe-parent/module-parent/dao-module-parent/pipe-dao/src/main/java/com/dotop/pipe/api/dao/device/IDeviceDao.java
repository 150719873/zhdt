package com.dotop.pipe.api.dao.device;

import com.dotop.pipe.core.dto.decive.DeviceDataDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.device.DeviceFieldVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.core.vo.report.DeviceCurrVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface IDeviceDao extends BaseDao<DeviceDto, DeviceVo> {

    @Override
    List<DeviceVo> list(DeviceDto deviceDto) throws DataAccessException;

    @Override
    DeviceVo get(DeviceDto deviceDto) throws DataAccessException;

    @Override
    void add(DeviceDto deviceDto) throws DataAccessException;

    @Override
    Integer edit(DeviceDto deviceDto) throws DataAccessException;

    @Override
    Integer del(DeviceDto deviceDto) throws DataAccessException;

    @Override
    Boolean isExist(DeviceDto deviceDto) throws DataAccessException;

    List<DeviceFieldVo> getDeviceField(@Param("enterpriseId") String enterpriseId,
                                       @Param("deviceCode") String deviceCode, @Param("isDel") Integer isDel) throws DataAccessException;

    List<DevicePropertyVo> getDeviceRealTime(DeviceDataDto deviceDataDto) throws DataAccessException;

    List<DeviceVo> getFmDevice(@Param("enterpriseId") String enterpriseId, @Param("isDel") Integer isDel)
            throws DataAccessException;

    public List<ProductVo> getDeviceCount(DeviceDto deviceDto) throws DataAccessException;

    public DevicePropertyVo getDeviceProperty(@Param("deviceId") String deviceId, @Param("code") String code,
                                              @Param("field") String field, @Param("operEid") String operEid, @Param("isDel") Integer isDel)
            throws DataAccessException;

    @MapKey("code")
    public Map<String, DeviceVo> mapAll(@Param("operEid") String operEid, @Param("isDel") Integer isDel)
            throws DataAccessException;

    public DevicePropertyVo getDevicePropertys(@Param("deviceId") String deviceId, @Param("code") String code,
                                               @Param("fields") List<String> fields, @Param("operEid") String operEid, @Param("isDel") Integer isDel)
            throws DataAccessException;

    public List<DevicePropertyVo> getDevicePropertyAll(@Param("operEid") String operEid, @Param("isDel") Integer isDel,
                                                       @Param("category") String category , @Param("fields") List<String> fields) throws DataAccessException;

    public List<DeviceVo> listBind(DeviceDto deviceDto);

    @MapKey("code")
    public Map<String, DeviceVo> getDeviceIdByCode(@Param("deviceCodes") List<String> keylist);

    Integer editScales(List<DeviceDto> deviceDtos);

    List<DeviceVo> listForApp(DeviceDto deviceDto) throws DataAccessException;

    List<DeviceCurrVo> listForCurr(DeviceDto deviceDto) throws DataAccessException;
}
