package com.dotop.pipe.web.factory.report;

import com.dotop.pipe.web.api.factory.report.INightReadingFactory;
import com.dotop.pipe.api.service.device.INightReadingService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.report.NightReadingBo;
import com.dotop.pipe.core.form.NightReadingForm;
import com.dotop.pipe.core.vo.report.NightReadingVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @date
 */
@Component
public class NightReadingFactoryImpl implements INightReadingFactory {

    private final static Logger logger = LogManager.getLogger(NightReadingFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private INightReadingService iNightReadingService;

    @Override
    public List<NightReadingVo> listByDevices(NightReadingForm nightReadingForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            NightReadingBo nightReadingBo = BeanUtils.copy(nightReadingForm, NightReadingBo.class);
            nightReadingBo.setEnterpriseId(loginCas.getEnterpriseId());
            Set<String> ctimes = DateUtils.listDates(DateUtils.parseDatetime(nightReadingBo.getStartDate()), DateUtils.parseDatetime(nightReadingBo.getEndDate()));
            nightReadingBo.setCtimes(ctimes);
            List<NightReadingVo> nightReadingVos = iNightReadingService.listDevices(nightReadingBo);
            List<NightReadingVo> nightReadingVoList = new ArrayList<>();
            nightReadingVos.forEach(nightReadingVo -> {
                long readDateTime = DateUtils.parseDate(nightReadingVo.getReadDate()).getTime();
                if (readDateTime >= DateUtils.parseDatetime(nightReadingForm.getStartDate()).getTime() && readDateTime <= DateUtils.parseDatetime(nightReadingForm.getEndDate()).getTime()) {
                    nightReadingVo.setTotalWater(subtract(nightReadingVo.getMaxWater(), nightReadingVo.getMinWater()));
                    nightReadingVo.setNightWater(subtract(nightReadingVo.getNightMaxWater(), nightReadingVo.getMinWater()));
                    nightReadingVoList.add(nightReadingVo);
                }
            });
            return nightReadingVoList;
        } catch (Exception e) {
            logger.error(LogMsg.to(e.getMessage(), e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "查询错误");
        }
    }

    private double subtract(Double value1, Double value2) {
        if (value1 == null || value2 == null) {
            return 0D;
        }
        double result = value1 - value2;
        if (result < 0) {
            return 0D;
        }
        BigDecimal bg = BigDecimal.valueOf(result);
        return bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
