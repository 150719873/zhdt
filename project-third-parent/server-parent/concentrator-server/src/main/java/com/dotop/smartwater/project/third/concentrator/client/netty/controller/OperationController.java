package com.dotop.smartwater.project.third.concentrator.client.netty.controller;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.BaseModel;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.ConcentratorForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DeviceForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DownLoadFileForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.UploadTimeForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.global.GlobalContext;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.vo.GprsInfo;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.service.OperationService;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: dingtong
 * @description: 服务端操作接口
 *
 * @create: 2019-06-12 09:25
 **/
@RestController
@RequestMapping("/api")
public class OperationController extends Base {

    @Autowired
    private OperationService operationService;

    /**
     * 读取上报时间
     */
    @PostMapping(value = "/readUploadTime", produces = GlobalContext.PRODUCES)
    public String readUploadTime(@RequestBody ConcentratorForm concentratorForm) throws InterruptedException {
        if (StrUtil.isBlank(concentratorForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        return respStringOk(operationService.readUploadTime(concentratorForm));
    }

    /**
     * 设置上报时间
     */
    @PostMapping(value = "/setUploadTime", produces = GlobalContext.PRODUCES)
    public String setUploadTime(@RequestBody UploadTimeForm uploadTimeForm) throws InterruptedException {
        if (StrUtil.isBlank(uploadTimeForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }

        if (StrUtil.isBlank(uploadTimeForm.getType())) {
            return respString(ResultCode.ParamIllegal, "上报周期不能为空", null);
        }

        if (StrUtil.isBlank(uploadTimeForm.getTime())) {
            return respString(ResultCode.ParamIllegal, "上报时间不能为空", null);
        }
        operationService.setUploadTime(uploadTimeForm);
        return respStringOk();
    }

    /**
     * 设置上报状态
     */
    @PostMapping(value = "/setUploadStatus", produces = GlobalContext.PRODUCES)
    public String setUploadStatus(@RequestBody UploadTimeForm uploadTimeForm) throws InterruptedException {
        if (StrUtil.isBlank(uploadTimeForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }

        if (StrUtil.isBlank(uploadTimeForm.getStatus())) {
            return respString(ResultCode.ParamIllegal, "设置状态不能为空", null);
        }
        operationService.setUploadStatus(uploadTimeForm);
        return respStringOk();

    }

    /**
     * 查看上报状态
     */
    @PostMapping(value = "/getUploadStatus", produces = GlobalContext.PRODUCES)
    public String getUploadStatus(@RequestBody UploadTimeForm uploadTimeForm) throws InterruptedException {
        if (StrUtil.isBlank(uploadTimeForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        return respStringOk(operationService.getUploadStatus(uploadTimeForm));
    }

    /**
     * 读档案
     */
    @PostMapping(value = "/readFile", produces = GlobalContext.PRODUCES)
    public String readFile(@RequestBody BaseModel baseModel) throws InterruptedException {
        if (StrUtil.isBlank(baseModel.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        return respStringOk(operationService.readFile(baseModel));
    }

    /**
     * 下载档案
     */
    @PostMapping(value = "/downloadFile", produces = GlobalContext.PRODUCES)
    public String downloadFile(@RequestBody DownLoadFileForm downLoadFileForm) throws InterruptedException {
        if (StrUtil.isBlank(downLoadFileForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        if (downLoadFileForm.getList() == null || downLoadFileForm.getList().size() == 0) {
            return respString(ResultCode.ParamIllegal, "水表信息列表不能为空", null);
        }
        return respStringOk(operationService.downloadFile(downLoadFileForm));
    }

    /**
     * 数据初始化
     */
    @PostMapping(value = "/dataInitialization", produces = GlobalContext.PRODUCES)
    public String dataInitialization(@RequestBody BaseModel baseModel) throws InterruptedException {
        if (StrUtil.isBlank(baseModel.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        return respStringOk(operationService.dataInitialization(baseModel));
    }

    /**
     * 读单表
     */
    @PostMapping(value = "/readOne", produces = GlobalContext.PRODUCES)
    public String readOne(@RequestBody DeviceForm deviceForm) throws InterruptedException {
        if (StrUtil.isBlank(deviceForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        if (StrUtil.isBlank(deviceForm.getDevnum())) {
            return respString(ResultCode.ParamIllegal, "表序号不能为空", null);
        }
        return respStringOk(operationService.readOne(deviceForm));
    }

    /**
     * 读所有表
     */
    @PostMapping(value = "/readAll", produces = GlobalContext.PRODUCES)
    public String readAll(@RequestBody BaseModel baseModel) throws InterruptedException {
        if (StrUtil.isBlank(baseModel.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        if (baseModel.getNos() == null || baseModel.getNos().size() == 0) {
            return respString(ResultCode.ParamIllegal, "表序号列表不能为空", null);
        }
        return respStringOk(operationService.readAll(baseModel));
    }

    /**
     * 读GPRS
     */
    @PostMapping(value = "/getGPRS", produces = GlobalContext.PRODUCES)
    public String getGPRS(@RequestBody BaseModel baseModel) throws InterruptedException {
        if (StrUtil.isBlank(baseModel.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        return respStringOk(operationService.getGPRS(baseModel));
    }

    /**
     * 设置GPRS
     */
    @PostMapping(value = "/setGPRS", produces = GlobalContext.PRODUCES)
    public String setGPRS(@RequestBody GprsInfo gprsInfo) throws InterruptedException {
        if (StrUtil.isBlank(gprsInfo.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        if (StrUtil.isBlank(gprsInfo.getIp())) {
            return respString(ResultCode.ParamIllegal, "IP不能为空", null);
        }
        if (StrUtil.isBlank(gprsInfo.getPort())) {
            return respString(ResultCode.ParamIllegal, "端口号不能为空", null);
        }
        operationService.setGPRS(gprsInfo);
        return respStringOk();
    }

    /**
     * 设置时钟
     */
    @PostMapping(value = "/setClock", produces = GlobalContext.PRODUCES)
    public String setClock(@RequestBody BaseModel baseModel) throws InterruptedException {
        if (StrUtil.isBlank(baseModel.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        operationService.setClock(baseModel);
        return respStringOk();
    }

    /**
     * 获取时钟
     */
    @PostMapping(value = "/getClock", produces = GlobalContext.PRODUCES)
    public String getClock(@RequestBody BaseModel baseModel) throws InterruptedException {
        if (StrUtil.isBlank(baseModel.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        return respStringOk(operationService.getClock(baseModel));
    }

    /**
     * 开阀
     */
    @PostMapping(value = "/open", produces = GlobalContext.PRODUCES)
    public String open(@RequestBody DeviceForm deviceForm) throws InterruptedException {
        if (StrUtil.isBlank(deviceForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        if (StrUtil.isBlank(deviceForm.getDevnum())) {
            return respString(ResultCode.ParamIllegal, "表序号不能为空", null);
        }
        return respStringOk(operationService.open(deviceForm));
    }

    /**
     * 关阀
     */
    @PostMapping(value = "/close", produces = GlobalContext.PRODUCES)
    public String close(@RequestBody DeviceForm deviceForm) throws InterruptedException {
        if (StrUtil.isBlank(deviceForm.getNum())) {
            return respString(ResultCode.ParamIllegal, "集中器编号不能为空", null);
        }
        if (StrUtil.isBlank(deviceForm.getDevnum())) {
            return respString(ResultCode.ParamIllegal, "表序号不能为空", null);
        }
        return respStringOk(operationService.close(deviceForm));
    }
}
