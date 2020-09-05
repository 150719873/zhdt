package com.dotop.pipe.api.client.fegin.water;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.water.form.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


//@FeignClient(name = "water-server26", fallbackFactory = WaterHystrixFallbackFactory.class, path = "/water-server/")
@FeignClient(name = "water-server", fallbackFactory = WaterHystrixFallbackFactory.class, path = "/water-server/")
public interface IWaterFeginClient {

    /**
     * 产品列表
     */
    @PostMapping(value = "/store/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String storeList(@RequestBody StoreProductForm storeProductForm, @RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket);

    /**
     * 产品
     */
    @PostMapping(value = "/store/getProductByNo", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String store(@RequestBody StoreProductForm storeProductForm, @RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket);

    /**
     * 库存余量
     */
    @PostMapping(value = "/store/getInventory", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String inventory(@RequestBody InventoryForm inventoryForm, @RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket);

    /**
     * 字典类别
     */
    @PostMapping(value = "/dictionary/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String dictList(@RequestBody DictionaryForm dictionaryForm, @RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) throws FrameworkRuntimeException;

    /**
     * 短信
     */
    @PostMapping(value = "/notice/sendSMS", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String addNotice(@RequestBody NoticeForm noticeForm) throws FrameworkRuntimeException;

    /**
     * 新增设备接口
     */
    @PostMapping(value = "/device/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String devicePage(@RequestBody DeviceForm device, @RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) throws FrameworkRuntimeException;


    /**
     * 管漏订阅绑定
     */
    @PostMapping(value = "/deviceSubscribe/bind", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String deviceSubscribeBind(@RequestBody DeviceForm device) throws FrameworkRuntimeException;

    /**
     * 管漏订阅删除
     */
    @PostMapping(value = "/deviceSubscribe/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    String deviceSubscribeDel(@RequestBody DeviceForm device) throws FrameworkRuntimeException;
}
