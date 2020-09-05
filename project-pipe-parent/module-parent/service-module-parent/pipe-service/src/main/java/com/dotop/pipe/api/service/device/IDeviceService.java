package com.dotop.pipe.api.service.device;

import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.vo.device.DeviceFieldVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.core.vo.report.DeviceCurrVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 设备
 *
 *
 * @date 2018年10月31日
 */
public interface IDeviceService extends BaseService<DeviceBo, DeviceVo> {

    /**
     * 分页
     */
    public Pagination<DeviceVo> page(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 列表
     */
    public List<DeviceVo> list(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 查询
     */
    public DeviceVo get(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 新增
     */
    public DeviceVo add(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 编辑
     */
    public DeviceVo edit(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 删除
     */
    public String del(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 通过编码或主键判断是否存在
     */
    public boolean isExist(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 通过产品判断是否存在
     */
    public boolean isExistByProductId(String productId) throws FrameworkRuntimeException;

    List<DeviceFieldVo> getDeviceField(String enterpriseId, String code) throws FrameworkRuntimeException;

    /**
     * 获取设备实时数据
     *
     * @param deviceIds
     * @param fieldType
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DevicePropertyVo> getDeviceRealTime(List<String> deviceIds, FieldTypeEnum[] fieldType, String operEid)
            throws FrameworkRuntimeException;

    /**
     * 获取流量计设备
     *
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceVo> getFmDevice(String enterpriseId) throws FrameworkRuntimeException;

    /**
     * 设备统计
     *
     * @param deviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    public Pagination<ProductVo> getDeviceCount(DeviceBo deviceBo) throws FrameworkRuntimeException;

    /**
     * 查询传感器 实时数据
     *
     * @param code
     * @param tag_str
     * @param operEid
     * @param operEid2
     * @return
     * @throws FrameworkRuntimeException
     */
    public DevicePropertyVo getDeviceProperty(String deviceId, String code, String field, String operEid)
            throws FrameworkRuntimeException;

    /**
     * 查询传感器 实时数据
     *
     * @param code
     * @param tag_str
     * @param operEid
     * @param operEid2
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DevicePropertyVo> getDevicePropertys(String deviceId, String code, List<String> fields, String operEid)
            throws FrameworkRuntimeException;

    public Map<String, DeviceVo> mapAll(String operEid) throws FrameworkRuntimeException;

    /**
     * 查询所有设备的最新属性
     *
     * @param operEid
     * @param dictionaryTypeSensortype
     * @return
     * @throws FrameworkRuntimeException
     */
    public List<DevicePropertyVo> getDevicePropertyAll(String operEid, String category, List<String> fields)
            throws FrameworkRuntimeException;

    public Pagination<DeviceVo> pageBind(DeviceBo deviceBo);

    public Map<String, DeviceVo> getDeviceIdByCode(List<String> keylist);

    void editScales(List<DeviceBo> deviceBos);

    public List<DeviceVo> listForApp(DeviceBo deviceBo) throws FrameworkRuntimeException;

    Pagination<DeviceCurrVo> getDeviceCurrPropertys(DeviceBo deviceBo) throws FrameworkRuntimeException;
}
