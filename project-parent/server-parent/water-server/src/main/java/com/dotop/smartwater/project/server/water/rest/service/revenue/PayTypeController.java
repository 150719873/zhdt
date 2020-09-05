package com.dotop.smartwater.project.server.water.rest.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IPayTypeFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.PayTypeForm;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: project-parent
 * @description: 关联业主的水费收费类型

 * @create: 2019-02-26 08:46
 */
@RestController

@RequestMapping("/paytype")
public class PayTypeController extends FoundationController implements BaseController<PayTypeForm> {

    @Autowired
    private IPayTypeFactory iPayTypeFactory;

    public static final String PAYTYPEID = "payTypeId";

    /**
     * 获取收费种类信息
     *
     * @param payTypeForm
     * @return
     */
    @PostMapping(value = "/find", produces = GlobalContext.PRODUCES)
    public String find(@RequestBody PayTypeForm payTypeForm) {
        if (payTypeForm.getPage() == null && payTypeForm.getPageCount() == null) {
            List<PayTypeVo> list = iPayTypeFactory.noPagingFind(payTypeForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, list);
        } else {
            Pagination<PayTypeVo> pagination = iPayTypeFactory.find(payTypeForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
        }
    }


    /**
     * 保存收费种类信息
     *
     * @param payTypeForm
     * @return
     */
    @PostMapping(value = "/save", produces = GlobalContext.PRODUCES)
    public String save(@RequestBody PayTypeForm payTypeForm) {
        VerificationUtils.string("name", payTypeForm.getName());
        auditLog(OperateTypeEnum.OPERATIONS_LOG, "运维日志", "【新增】-【" + payTypeForm.getName() + "】");
        iPayTypeFactory.savePayType(payTypeForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


    /**
     * 修改收费种类信息
     *
     * @param payTypeForm
     * @return
     */
    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody PayTypeForm payTypeForm) {
        VerificationUtils.string(PAYTYPEID, payTypeForm.getTypeid());
        VerificationUtils.string("name", payTypeForm.getName());
        PayTypeVo payTypeVo = iPayTypeFactory.getPayType(payTypeForm);

        auditLog(OperateTypeEnum.OPERATIONS_LOG, "运维日志", "【修改】-【" + payTypeVo.getName() + "】为【" + payTypeForm.getName() + "】");
        iPayTypeFactory.editPayType(payTypeForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 获取收费种类
     */
    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody PayTypeForm payTypeForm) {
        VerificationUtils.string(PAYTYPEID, payTypeForm.getTypeid());
        PayTypeVo payTypeVo = iPayTypeFactory.getPayType(payTypeForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, payTypeVo);
    }


    /**
     * 删除收费种类
     *
     * @param payTypeForm
     * @return
     */
    @PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
    public String deletePayType(@RequestBody PayTypeForm payTypeForm) {
        VerificationUtils.string(PAYTYPEID, payTypeForm.getTypeid());
        PayTypeVo payTypeVo = iPayTypeFactory.getPayType(payTypeForm);
        auditLog(OperateTypeEnum.OPERATIONS_LOG, "运维日志", "【删除】-【" + payTypeVo.getName() + "】");
        iPayTypeFactory.delete(payTypeForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


    @PostMapping(value = "/editPayTypeTask", produces = GlobalContext.PRODUCES)
    public String editPayTypeTask(@RequestBody PayTypeForm payTypeForm) {
        VerificationUtils.string(PAYTYPEID, payTypeForm.getTypeid());
        VerificationUtils.string("taskMonth", payTypeForm.getTaskMonth());
        VerificationUtils.string("taskDay", payTypeForm.getTaskDay());
        VerificationUtils.obj("taskSwitch", payTypeForm.getTaskSwitch());
        VerificationUtils.integer("taskIntervalDay", payTypeForm.getTaskIntervalDay());

        auditLog(OperateTypeEnum.OPERATIONS_LOG, "运维日志", "【修改】- 收费种类定时任务 - 【" + payTypeForm.getTypeid() + "】");
        payTypeForm.setUpdated(new Date());
        iPayTypeFactory.updatePayType(payTypeForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
}
