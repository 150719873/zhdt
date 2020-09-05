package com.dotop.smartwater.project.module.api.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.revenue.IPayTypeFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderPriceBo;
import com.dotop.smartwater.project.module.core.water.bo.PayTypeBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.CompriseForm;
import com.dotop.smartwater.project.module.core.water.form.LadderForm;
import com.dotop.smartwater.project.module.core.water.form.LadderPriceForm;
import com.dotop.smartwater.project.module.core.water.form.PayTypeForm;
import com.dotop.smartwater.project.module.core.water.vo.CompriseVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收费类型
 *

 * @date 2019年2月25日
 */
@Component
public class PayTypeFactoryImpl implements IPayTypeFactory {

    @Autowired
    private IPayTypeService iPayTypeService;

    @Override
    public List<PayTypeVo> noPagingFind(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);
        payTypeBo.setEnterpriseid(user.getEnterpriseid());
        return iPayTypeService.noPagingFind(payTypeBo);
    }

    @Override
    public Pagination<PayTypeVo> find(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);
        payTypeBo.setEnterpriseid(user.getEnterpriseid());

        Pagination<PayTypeVo> pagination = iPayTypeService.find(payTypeBo);
        return pagination;
    }

    @Override
    public void updatePayType(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);
        iPayTypeService.updatePayType(payTypeBo);
        // TODO 修改收费种类日志
    }


    @Override
    public String addPayType(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);
        payTypeBo.setEnterpriseid(user.getEnterpriseid());
        payTypeBo.setCreateuser(user.getUserid());
        payTypeBo.setCreatetime(DateUtils.formatDatetime(new Date()));
        payTypeBo.setUsername(user.getName());

        PayTypeVo payTypeVo = iPayTypeService.addPayType(payTypeBo);

        return payTypeVo.getTypeid();
    }

    @Override
    public PayTypeVo getPayType(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);
        return iPayTypeService.getPayType(payTypeBo);
    }


    @Override
    public void editPayType(PayTypeForm payTypeForm) {
        UserVo user = AuthCasClient.getUser();
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);
        payTypeBo.setEnterpriseid(user.getEnterpriseid());
        // 验证费用名称是否已存在
        if (iPayTypeService.checkNameIsExist(payTypeBo)) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "费用名称已存在");
        }

        payTypeBo.setCreateuser(user.getUserid());
        payTypeBo.setCreatetime(DateUtils.formatDatetime(new Date()));
        payTypeBo.setUsername(user.getName());

        //费用组成
        if (payTypeForm.getComprises() != null) {
            List<CompriseBo> comprises = new ArrayList<CompriseBo>();
            for (CompriseForm compriseForm : payTypeForm.getComprises()) {
                CompriseBo comprisebo = new CompriseBo();
                BeanUtils.copyProperties(compriseForm, comprisebo);
                comprisebo.setTypeid(payTypeBo.getTypeid());
                comprisebo.setId(UuidUtils.getUuid());
                comprisebo.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                comprises.add(comprisebo);
            }
            payTypeBo.setComprises(comprises);
        }


        //阶梯收费
        if (payTypeForm.getLadders() != null) {
            List<LadderBo> ladders = new ArrayList<LadderBo>();
            for (LadderForm ladderForm : payTypeForm.getLadders()) {
                LadderBo ladder = new LadderBo();
                BeanUtils.copyProperties(ladderForm, ladder);
                ladder.setTypeid(payTypeBo.getTypeid());
                ladder.setId(UuidUtils.getUuid());
                ladder.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                //费用详情
                List<LadderPriceBo> ladderPrices = new ArrayList<LadderPriceBo>();
                for (LadderPriceForm priceForm : ladderForm.getLadderPrices()) {
                    LadderPriceBo ladderPrice = new LadderPriceBo();
                    BeanUtils.copyProperties(priceForm, ladderPrice);
                    ladderPrice.setLadderid(ladder.getId());
                    ladderPrice.setId(UuidUtils.getUuid());
                    ladderPrice.setTypeid(payTypeBo.getTypeid());
                    ladderPrice.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    ladderPrices.add(ladderPrice);
                }

                ladder.setLadderPrices(ladderPrices);
                ladders.add(ladder);
            }
            payTypeBo.setLadders(ladders);
        }

        iPayTypeService.editPayType(payTypeBo);
    }

    @Override
    public PayTypeVo savePayType(PayTypeForm payTypeForm) {
        UserVo user = AuthCasClient.getUser();
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);

        // 验证费用名称是否已存在
        payTypeBo.setEnterpriseid(user.getEnterpriseid());
        if (iPayTypeService.checkNameIsExist(payTypeBo)) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "费用名称已存在");
        }

        payTypeBo.setTypeid(UuidUtils.getUuid());
        payTypeBo.setEnterpriseid(user.getEnterpriseid());
        payTypeBo.setCreateuser(user.getUserid());
        payTypeBo.setCreatetime(DateUtils.formatDatetime(new Date()));
        payTypeBo.setUsername(user.getName());

        //费用组成
        if (payTypeForm.getComprises() != null) {
            List<CompriseBo> comprises = new ArrayList<CompriseBo>();
            for (CompriseForm compriseForm : payTypeForm.getComprises()) {
                CompriseBo comprisebo = new CompriseBo();
                BeanUtils.copyProperties(compriseForm, comprisebo);
                comprisebo.setTypeid(payTypeBo.getTypeid());
                comprisebo.setId(UuidUtils.getUuid());
                comprisebo.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                comprises.add(comprisebo);
            }
            payTypeBo.setComprises(comprises);
        }


        //阶梯收费
        if (payTypeForm.getLadders() != null) {
            List<LadderBo> ladders = new ArrayList<LadderBo>();
            for (LadderForm ladderForm : payTypeForm.getLadders()) {
                LadderBo ladder = new LadderBo();
                BeanUtils.copyProperties(ladderForm, ladder);
                ladder.setTypeid(payTypeBo.getTypeid());
                ladder.setId(UuidUtils.getUuid());
                ladder.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                //费用详情
                List<LadderPriceBo> ladderPrices = new ArrayList<LadderPriceBo>();
                for (LadderPriceForm priceForm : ladderForm.getLadderPrices()) {
                    LadderPriceBo ladderPrice = new LadderPriceBo();
                    BeanUtils.copyProperties(priceForm, ladderPrice);
                    ladderPrice.setLadderid(ladder.getId());
                    ladderPrice.setId(UuidUtils.getUuid());
                    ladderPrice.setTypeid(payTypeBo.getTypeid());
                    ladderPrice.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    ladderPrices.add(ladderPrice);
                }

                ladder.setLadderPrices(ladderPrices);
                ladders.add(ladder);
            }
            payTypeBo.setLadders(ladders);
        }
        return iPayTypeService.savePayType(payTypeBo);
    }


    @Override
    public void updatePayTypeComprise(CompriseForm compriseForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        CompriseBo compriseBo = new CompriseBo();
        BeanUtils.copyProperties(compriseForm, compriseBo);
        compriseBo.setEnterpriseid(user.getEnterpriseid());
        compriseBo.setCreatetime(DateUtils.formatDatetime(new Date()));
        iPayTypeService.updatePayTypeComprise(compriseBo);
    }

    @Override
    public String addPayTypeComprise(CompriseForm compriseForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        CompriseBo compriseBo = new CompriseBo();
        BeanUtils.copyProperties(compriseForm, compriseBo);
        compriseBo.setEnterpriseid(user.getEnterpriseid());
        compriseBo.setCreatetime(DateUtils.formatDatetime(new Date()));
        CompriseVo compriseVo = iPayTypeService.addPayTypeComprise(compriseBo);
        return compriseVo.getId();
    }

    @Override
    public void saveLadder(LadderForm ladderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        LadderBo ladderBo = new LadderBo();
        BeanUtils.copyProperties(ladderForm, ladderBo);
        ladderBo.setEnterpriseid(user.getEnterpriseid());
        ladderBo.setCreatetime(DateUtils.formatDatetime(new Date()));
        iPayTypeService.saveLadder(ladderBo);
    }

    @Override
    public void editLadder(LadderForm ladderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        LadderBo ladderBo = new LadderBo();
        BeanUtils.copyProperties(ladderForm, ladderBo);
        iPayTypeService.editLadder(ladderBo);
    }

    @Override
    public List<CompriseVo> findComprise(CompriseForm compriseForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        CompriseBo compriseBo = new CompriseBo();
        BeanUtils.copyProperties(compriseForm, compriseBo);

        return iPayTypeService.findComprise(compriseBo);
    }

    @Override
    public List<LadderVo> findLadders(LadderForm ladderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        LadderBo ladderBo = new LadderBo();
        BeanUtils.copyProperties(ladderForm, ladderBo);
        return iPayTypeService.findLadders(ladderBo);
    }

    @Override
    public int getMaxLadder(LadderForm ladderForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();

        LadderBo ladderBo = new LadderBo();
        BeanUtils.copyProperties(ladderForm, ladderBo);

        ladderBo.setEnterpriseid(user.getEnterpriseid());

        int ladderNo = 0;
        LadderVo maxLadder = iPayTypeService.getMaxLadder(ladderBo);
        if (maxLadder == null) {
            ladderNo++;
        } else {
            ladderNo = maxLadder.getLadderno() + 1;
        }
        return ladderNo;
    }

    @Override
    public void delete(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        PayTypeBo payTypeBo = new PayTypeBo();
        BeanUtils.copyProperties(payTypeForm, payTypeBo);

        int count = iPayTypeService.checkPayTypeIsUse(payTypeBo);
        if (count > 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "当前收费种类已使用，无法删除");
        }
        iPayTypeService.delete(payTypeBo);
    }

    @Override
    public void deleteComprise(CompriseForm compriseForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        CompriseBo compriseBo = new CompriseBo();
        BeanUtils.copyProperties(compriseForm, compriseBo);

        iPayTypeService.deleteComprise(compriseBo);
    }

    @Override
    public void deleteLadder(LadderForm ladderForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();

        LadderBo ladderBo = new LadderBo();
        BeanUtils.copyProperties(ladderForm, ladderBo);

        iPayTypeService.deleteLadder(ladderBo);
    }

    @Override
    public PayTypeVo get(PayTypeForm payTypeForm) throws FrameworkRuntimeException {
        String typeid = payTypeForm.getTypeid();
        return iPayTypeService.get(typeid);
    }
}
