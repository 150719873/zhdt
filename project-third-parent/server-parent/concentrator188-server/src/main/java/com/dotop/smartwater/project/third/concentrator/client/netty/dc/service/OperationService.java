package com.dotop.smartwater.project.third.concentrator.client.netty.dc.service;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.*;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo.GprsInfo;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo.UploadTimeVo;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterFileBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterReadBo;

import java.util.List;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-17 09:45
 **/
public interface OperationService {
    /***
     * 读取上报时间
     * ***/
    UploadTimeVo readUploadTime(ConcentratorForm concentratorForm) throws InterruptedException;

    /***
     * 设置上报时间
     * ***/
    void setUploadTime(UploadTimeForm uploadTimeForm) throws InterruptedException;

    /***
     * 设置上报是否允许
     * ***/
    void setUploadStatus(UploadTimeForm uploadTimeForm) throws InterruptedException;

    /***
     * 获取上报状态
     * ***/
    String getUploadStatus(UploadTimeForm uploadTimeForm) throws InterruptedException;

    /***
     * 读档案
     * ***/
    List<TerminalMeterFileBo> readFile(BaseModel baseModel) throws InterruptedException;

    /***
     * 下载档案
     * ***/
    String downloadFile(DownLoadFileForm downLoadFileForm) throws InterruptedException;

    /***
     * 初始化档案
     * ***/
    String dataInitialization(BaseModel baseModel) throws InterruptedException;

    /***
     * 抄读单表
     * ***/
    TerminalMeterReadBo readOne(DeviceForm deviceForm) throws InterruptedException;

    /***
     * 抄读集中器所有表
     * ***/
    List<TerminalMeterReadBo> readAll(BaseModel baseModel) throws InterruptedException;

    /***
     * 发送自己的命令(后门)
     * ***/
    String send(Demo demo) throws InterruptedException;

    /***
     * 获取集中器服务器地址
     * ***/
    GprsInfo getGPRS(BaseModel baseModel) throws InterruptedException;

    /***
     * 设置集中器服务器地址
     * ***/
    void setGPRS(GprsInfo gprsInfo) throws InterruptedException;

    /***
     * 获取集中器时间
     * ***/
    String getClock(BaseModel baseModel) throws InterruptedException;

    /***
     * 设置集中器时间
     * ***/
    void setClock(BaseModel baseModel) throws InterruptedException;

    /***
     * true 阻塞中
     * ***/
    boolean lock(String enterpriseId, String concentratorCode);

    /***
     * 开阀
     * ***/
    String open(DeviceForm deviceForm) throws InterruptedException;

    /***
     * 关阀
     * ***/
    String close(DeviceForm deviceForm) throws InterruptedException;

}
