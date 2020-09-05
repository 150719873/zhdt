package com.dotop.pipe.server.rest.service.water;

import com.dotop.pipe.api.client.core.WaterDeviceForm;
import com.dotop.pipe.api.client.core.WaterDeviceVo;
import com.dotop.pipe.web.api.factory.water.IWaterDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController()
@RequestMapping("/water/device")
public class WaterDeviceController implements BaseController<WaterDeviceForm> {

    private final static Logger logger = LogManager.getLogger(WaterDeviceController.class);

    @Autowired
    private IWaterDeviceFactory iWaterDeviceFactory;

    /**
     * 分页查询
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody WaterDeviceForm waterDeviceForm) {
        logger.info(LogMsg.to("msg:", " waterDeviceForm", "deviceForm", waterDeviceForm));
        Integer page = waterDeviceForm.getPage();
        Integer pageSize = waterDeviceForm.getPageSize();
        String deveui = waterDeviceForm.getDeveui();
        String devno = waterDeviceForm.getDevno();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("deveui", deveui, true);
        VerificationUtils.string("devno", devno, true);
        Pagination<WaterDeviceVo> pagination = iWaterDeviceFactory.page(waterDeviceForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }


}
