package com.dotop.pipe.api.client;

import java.util.ArrayList;
import java.util.List;

import com.dotop.pipe.api.client.fegin.water.IWaterFeginClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.form.NoticeForm;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;

@Component
public class WaterNoticeClientImpl implements IWaterNoticeClient {

    private final static Logger logger = LogManager.getLogger(WaterNoticeClientImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IWaterFeginClient iWaterFeginClient;

    @Override
    public void add(AlarmBo alarmBo, AlarmNoticeRuleVo alarmNoticeVo) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get(false);
        // 调用接口
        NoticeForm noticeForm = new NoticeForm();
        // 组装信息 发送的内容信息
        noticeForm.setTitle("管漏系统设备预警通知");
        noticeForm.setBody(alarmBo.getName());
        noticeForm.setSendWay(alarmNoticeVo.getNotifyType()); // 发送类型 SMS EMAIL SYS
        List<String> sendWayList = new ArrayList();
        sendWayList.add(alarmNoticeVo.getNotifyType());
        noticeForm.setSendWayList(sendWayList);
        noticeForm.setModelType(alarmNoticeVo.getModelType()); // 设备预警
        noticeForm.setEnterpriseid(alarmNoticeVo.getEnterpriseId());
        noticeForm.setReceiveWay(alarmNoticeVo.getNotifyUserType()); // 接收类型

        /*ReceiveObjectVo receiveObjectVo = new ReceiveObjectVo();
        receiveObjectVo.setId(alarmNoticeVo.getNotifyUserid()); // 李士成id*/
        String[] userids = alarmNoticeVo.getNotifyUserid().split(",");
        List<ReceiveObjectVo> list = new ArrayList();
        for (String userid:userids) {
            ReceiveObjectVo receiveObjectVo = new ReceiveObjectVo();
            receiveObjectVo.setId(userid); // 李士成id
            list.add(receiveObjectVo);
        }
        noticeForm.setReceiveObjList(list);
        System.out.println("发送短信接口");
        String result = iWaterFeginClient.addNotice(noticeForm);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
    }
}
