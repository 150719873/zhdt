package com.dotop.pipe.web.api.factory.device;

import com.dotop.pipe.core.form.DeviceDataForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceFieldVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.device.DijkstraVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;
import java.util.Map;

public interface IDeviceFactory extends BaseFactory<DeviceForm, DeviceVo> {

    @Override
    Pagination<DeviceVo> page(DeviceForm deviceForm) throws FrameworkRuntimeException;

    @Override
    DeviceVo get(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<DeviceVo> adds(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<DeviceVo> editReturnList(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<DeviceVo> editCoordinate(DeviceForm deviceForm) throws FrameworkRuntimeException;

    @Override
    List<DeviceVo> list(DeviceForm deviceForm) throws FrameworkRuntimeException;

    @Override
    String del(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<DeviceFieldVo> getDeviceField(String deviceCode) throws FrameworkRuntimeException;

    Map<String, List<DevicePropertyVo>> getDeviceRealTime(DeviceDataForm deviceDataForm)
            throws FrameworkRuntimeException;

    List<DeviceVo> getFmDevice() throws FrameworkRuntimeException;

    Pagination<ProductVo> getDeviceCount(DeviceForm deviceForm) throws FrameworkRuntimeException;

    DeviceVo mergePipe(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<DeviceVo> addPipes(DeviceForm deviceForm);

    Pagination<DeviceVo> pageBind(DeviceForm deviceForm);

    /**
     * 片区设备绑定设备
     *
     * @param deviceForm
     */
    void bindAdd(DeviceForm deviceForm);

    void bindDel(DeviceForm deviceForm);

    /**
     * 批量编辑比例尺
     * @param deviceForms
     */
    void editScales(List<DeviceForm> deviceForms);

    DijkstraVo connectedCal(DeviceForm deviceForm);

    List<DeviceVo> listForApp(DeviceForm deviceForm) throws FrameworkRuntimeException;

}
