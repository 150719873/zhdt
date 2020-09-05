package com.dotop.smartwater.project.third.module.client.fegin;

import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerChangeForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 对接内部系统调用接口
 *
 *
 */
@FeignClient(name = "water-server", fallbackFactory = WaterHystrixFallbackFactory.class, path = "/water-server/")
public interface IWaterFeginClient {

    /**
     * 新增客户接口
     */
    @PostMapping(value = "/ownerFac/adds", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String ownerAdds(@RequestBody List<OwnerForm> owners, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 编辑客户接口
     */
    @PostMapping(value = "/ownerFac/edits", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String ownerEdits(@RequestBody List<OwnerForm> owners, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 换表客户接口
     */
    @PostMapping(value = "/ownerFac/changes", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String ownerChanges(@RequestBody List<OwnerChangeForm> ownerChanges, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 新增设备接口
     */
    @PostMapping(value = "/device/adds", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String deviceAdds(@RequestBody List<DeviceForm> devices, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 编辑设备接口
     */
    @PostMapping(value = "/device/edits", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String deviceEdits(@RequestBody List<DeviceForm> devices, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 推送读数接口
     */
    @PostMapping(value = "/device/third/uplinks", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String uplinks(@RequestBody List<DeviceUplinkForm> waters, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 新增下发命令接口
     */
    @PostMapping(value = "/device/third/downlink", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String downlink(@RequestBody WaterDownLoadForm waterDownLoadForm, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

    /**
     * 编辑命令结果接口
     */
    @PostMapping(value = "/device/third/downlink/result", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String downlinkEdit(@RequestBody CommandBo commandBo, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid);

}
