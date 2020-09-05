package com.dotop.pipe.server.rest.service.app;

import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 爆管管理入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/app")
public class AppController implements BaseController<DeviceForm> {

    private static final Logger logger = LogManager.getLogger(AppController.class);

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("deviceForm", deviceForm));
//        Integer limit = deviceForm.getLimit();
        String productCategory = deviceForm.getProductCategory();
        String productType = deviceForm.getProductType();
        String name = deviceForm.getName();
        // 验证
//        VerificationUtils.integer("limit", limit);
        VerificationUtils.string("productCategory", productCategory);
        VerificationUtils.string("productType", productType);
        VerificationUtils.string("name", name,true);
        String type = "all";
        // 条件过滤，如果为"all"，则表示全部类型
        if (type.equals(productType)) {
            deviceForm.setProductType(null);
        }
        List<DeviceVo> list = iDeviceFactory.listForApp(deviceForm);
        return resp(list);
    }

}
