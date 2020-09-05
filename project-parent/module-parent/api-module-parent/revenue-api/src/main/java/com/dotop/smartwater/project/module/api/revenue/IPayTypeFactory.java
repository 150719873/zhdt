package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.CompriseForm;
import com.dotop.smartwater.project.module.core.water.form.LadderForm;
import com.dotop.smartwater.project.module.core.water.form.PayTypeForm;
import com.dotop.smartwater.project.module.core.water.vo.CompriseVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;

import java.util.List;


/**
 * @program: project-parent
 * @description: 收费类型

 * @create: 2019-02-26 09:23
 **/
public interface IPayTypeFactory extends BaseFactory<PayTypeForm, PayTypeVo> {

    List<PayTypeVo> noPagingFind(PayTypeForm payTypeForm);

    Pagination<PayTypeVo> find(PayTypeForm payTypeForm);

    void updatePayType(PayTypeForm payTypeForm);
    
    /**
     * 修改收费种类信息
     * @param payTypeForm
     */
    void editPayType(PayTypeForm payTypeForm);
    
    /**
     * 获取详情
     */
    PayTypeVo getPayType(PayTypeForm payTypeForm);

    
    String addPayType(PayTypeForm payTypeForm);
    
    /**
     * 保存收费种类信息
     * @param payTypeForm
     * @return
     */
    PayTypeVo savePayType(PayTypeForm payTypeForm);
    

    void updatePayTypeComprise(CompriseForm compriseForm);

    String addPayTypeComprise(CompriseForm compriseForm);

    void saveLadder(LadderForm ladderForm);

    void editLadder(LadderForm ladderForm);

    List<CompriseVo> findComprise(CompriseForm compriseForm);

    List<LadderVo> findLadders(LadderForm ladderForm);

    int getMaxLadder(LadderForm ladderForm);

    void delete(PayTypeForm payTypeForm);

    void deleteComprise(CompriseForm compriseForm);

    void deleteLadder(LadderForm ladderForm);
}
