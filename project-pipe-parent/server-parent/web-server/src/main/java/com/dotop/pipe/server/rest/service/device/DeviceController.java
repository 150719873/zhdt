package com.dotop.pipe.server.rest.service.device;

import com.dotop.pipe.core.form.DeviceDataForm;
import com.dotop.pipe.core.form.DeviceExtForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.device.DeviceFieldVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.device.DijkstraVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @date 2019年1月16日
 */
@RestController()
@RequestMapping("/device")
public class DeviceController implements BaseController<DeviceForm> {

    private final static Logger logger = LogManager.getLogger(DeviceController.class);

    @Autowired
    private IDeviceFactory iDeviceFactory;

    /**
     * 分页查询
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageSize = deviceForm.getPageSize();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("productCategory", productCategory);
        Pagination<DeviceVo> pagination = iDeviceFactory.page(deviceForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }

    /**
     * 分页查询,无产品类型校验
     */
    @PostMapping(value = "/pageScale", produces = GlobalContext.PRODUCES)
    public String pageScale(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageSize = deviceForm.getPageSize();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("productCategory", productCategory, true);
        Pagination<DeviceVo> pagination = iDeviceFactory.page(deviceForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }

    /**
     * 批量编辑比例尺
     *
     * @param deviceForms
     */
    @PostMapping(value = "/editScales", produces = GlobalContext.PRODUCES)
    public void eidtScales(@RequestBody List<DeviceForm> deviceForms) {
        logger.info(LogMsg.to("msg:", " 批量编辑比例尺开始", "deviceForm", deviceForms));
        iDeviceFactory.editScales(deviceForms);
        logger.info(LogMsg.to("msg:", " 批量编辑比例尺结束"));
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("deviceForm", deviceForm));
        Integer limit = deviceForm.getLimit();
        String productCategory = deviceForm.getProductCategory();
        String productType = deviceForm.getProductType();
        // 验证
        VerificationUtils.integer("limit", limit);
        VerificationUtils.string("productCategory", productCategory);
        VerificationUtils.string("productType", productType);
        String type = "all";
        // 条件过滤，如果为"all"，则表示全部类型
        if (type.equals(productType)) {
            deviceForm.setProductType(null);
        }
        List<DeviceVo> list = iDeviceFactory.list(deviceForm);
        return resp(list);
    }

    /**
     * 详情
     */
    @Override
    @GetMapping(value = "/{deviceId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "详情查询开始", "deviceForm", deviceForm));
        String deviceId = deviceForm.getDeviceId();
        // 验证
        VerificationUtils.string("deviceId", deviceId);
        DeviceVo deviceVo = iDeviceFactory.get(deviceForm);
        logger.info(LogMsg.to("msg:", "详情查询结束"));
        return resp(deviceVo);
    }

    /**
     * 新增
     */
    @Override
    @PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "新增开始", "deviceForm", deviceForm));
        String areaId = deviceForm.getAreaId();
        String productId = deviceForm.getProductId();
        // 校验区域id
        // VerificationUtils.string("areaId", areaId);
        // 产品id
        VerificationUtils.string("productId", productId);
        List<PointForm> points = deviceForm.getPoints();
        // 关于管道的批量处理扩展实体类
        List<DeviceExtForm> deviceExtForms = deviceForm.getDeviceExts();
        List<DeviceVo> list = null;
        if (points != null) {
            // 普通设备的批量新增
            VerificationUtils.objList("points", points);
            VerificationUtils.string("code", deviceForm.getCode());
            VerificationUtils.string("name", deviceForm.getName());
            VerificationUtils.string("des", deviceForm.getDes(), true, 100);
            list = iDeviceFactory.adds(deviceForm);
        }
        if (deviceExtForms != null) {
            // 管道的批量处理
            for (DeviceExtForm deviceExtForm : deviceExtForms) {
                VerificationUtils.string("code", deviceExtForm.getCode());
                VerificationUtils.string("name", deviceExtForm.getName());
                VerificationUtils.string("des", deviceExtForm.getDes(), true, 100);
            }
            list = iDeviceFactory.addPipes(deviceForm);
        }
        return resp(list);
    }

    /**
     * 修改
     */
    @Override
    @PutMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("deviceForm", deviceForm));
        String deviceId = deviceForm.getDeviceId();
        String code = deviceForm.getCode();
        String name = deviceForm.getName();
        String des = deviceForm.getDes();
        String areaId = deviceForm.getAreaId();
        // 校验
        VerificationUtils.string("deviceId", deviceId);
        VerificationUtils.string("code", code);
        VerificationUtils.string("name", name);
        VerificationUtils.string("des", des, true, 100);
        VerificationUtils.string("areaId", areaId);
        List<DeviceVo> list = iDeviceFactory.editReturnList(deviceForm);
        return resp(list);
    }

    /**
     * 修改坐标
     *
     * @param deviceForm
     * @return
     */
    @PutMapping(value = "/{editCoordinate}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editCoordinate(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("deviceForm", deviceForm));
        String deviceId = deviceForm.getDeviceId();
        String code = deviceForm.getCode();
        String name = deviceForm.getName();
        String des = deviceForm.getDes();
        String areaId = deviceForm.getAreaId();
        // 校验
        VerificationUtils.string("deviceId", deviceId);
        VerificationUtils.string("code", code);
        VerificationUtils.string("name", name);
        VerificationUtils.string("des", des, true, 100);
        VerificationUtils.string("areaId", areaId);
        List<DeviceVo> list = iDeviceFactory.editCoordinate(deviceForm);
        return resp(list);
    }

    /**
     * 删除
     */
    @Override
    @DeleteMapping(value = "/{deviceId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "删除 开始", deviceForm, deviceForm));
        String deviceId = deviceForm.getDeviceId();
        // 校验
        VerificationUtils.string("deviceId", deviceId);
        String count = iDeviceFactory.del(deviceForm);
        logger.info(LogMsg.to("msg:", "删除 结束", "更新数据", count));
        return resp();
    }

    /**
     * 合并管道
     *
     * @param deviceForm
     * @return
     */
    @PutMapping(value = "/merge", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String mergePipe(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "合并管道开始", deviceForm, deviceForm));
        List<String> deviceIds = deviceForm.getMergePipeIds();
        List<PointForm> points = deviceForm.getPoints();
        // 校验
        VerificationUtils.objList("deviceIds", deviceIds);
        VerificationUtils.objList("points", points);
        // 返回新生成的一条管道
        DeviceVo deviceVo = iDeviceFactory.mergePipe(deviceForm);
        logger.info(LogMsg.to("msg:", "合并管道结束"));
        return resp(deviceVo);
    }

    // 实时统计报表 查询设备列表 流量计 压力计 水质计等设备
    @GetMapping(value = {"/getDeviceField/{deviceCode}",
            "/getDeviceField"}, produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getDeviceField(@PathVariable(name = "deviceCode", required = false) String deviceCode) {
        logger.info(LogMsg.to("msg:", "查询设备开始", "deviceCode", deviceCode));
        List<DeviceFieldVo> list = iDeviceFactory.getDeviceField(deviceCode);
        logger.info(LogMsg.to("msg:", "查询设备结束"));
        return resp(list);
    }

    /**
     * map 页面实时流量显示 调用
     *
     * @param deviceDataForm
     * @return
     */
    @PostMapping(value = "/getDeviceRealTime", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getDeviceRealTime(@RequestBody DeviceDataForm deviceDataForm) {
        logger.info(LogMsg.to("msg:", "计量计实时数据查询开始"));
        logger.info(LogMsg.to("deviceDataForm", deviceDataForm));
        System.out.println(deviceDataForm);
        // 验证
        Map<String, List<DevicePropertyVo>> map = iDeviceFactory.getDeviceRealTime(deviceDataForm);
        logger.info(LogMsg.to("msg:", "计量计实时数据查询结束"));
        return resp(map);
    }

    /**
     * 查询虚拟流量计设备
     *
     * @return
     */
    @GetMapping(value = "/getFmDevice", produces = GlobalContext.PRODUCES)
    public String getFmDevice() {
        logger.info(LogMsg.to("msg:", "获取流量计设备数据查询开始"));
        List<DeviceVo> list = iDeviceFactory.getFmDevice();
        logger.info(LogMsg.to("msg:", "获取流量计设备数据结束"));
        return resp(list);
    }

    /**
     * 设备统计
     *
     * @return
     */
    @GetMapping(value = {"/getDeviceCount/{page}/{pageSize}/{productCategory}",
            "/getDeviceCount/{page}/{pageSize}/{productCategory}/{enterpriseId}"}, produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String getDeviceCount(DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "设备统计查询开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageSize = deviceForm.getPageSize();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("productCategory", productCategory);
        Pagination<ProductVo> list = iDeviceFactory.getDeviceCount(deviceForm);
        logger.info(LogMsg.to("msg:", "设备统计结束"));
        return resp(list);
    }

    /**
     * 片区设备绑定
     */
    @PostMapping(value = "/page/bind", produces = GlobalContext.PRODUCES)
    public String pageBind(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", deviceForm));
        Integer page = deviceForm.getPage();
        Integer pageSize = deviceForm.getPageSize();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageSize", pageSize);
        VerificationUtils.string("productCategory", productCategory, true);
        Pagination<DeviceVo> pagination = iDeviceFactory.pageBind(deviceForm);
        logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
        return resp(pagination);
    }

    /**
     * 片区设备绑定新增
     */
    @PostMapping(value = "/bind/add", produces = GlobalContext.PRODUCES)
    public String bindAdd(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "片区设备绑定新增开始", "deviceForm", deviceForm));
        VerificationUtils.string("areaId", deviceForm.getDeviceId());
        VerificationUtils.string("otherId", deviceForm.getOtherId());
        iDeviceFactory.bindAdd(deviceForm);
        return resp();
    }

    @PostMapping(value = "/bind/del", produces = GlobalContext.PRODUCES)
    public String bindDel(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("msg:", "片区设备绑定新增开始", "deviceForm", deviceForm));
        VerificationUtils.string("deviceMapId", deviceForm.getDeviceMapId());
        iDeviceFactory.bindDel(deviceForm);
        return resp();
    }

    /**
     * 连通性分析计算接口
     */
    @PostMapping(value = "/connectedCal", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String connectedCal(@RequestBody DeviceForm deviceForm) {
        logger.info(LogMsg.to("deviceForm", deviceForm));
        Integer limit = deviceForm.getLimit();
        String productCategory = deviceForm.getProductCategory();
        // 验证
        VerificationUtils.integer("limit", limit);
        VerificationUtils.string("productCategory", productCategory);
        DijkstraVo paths = iDeviceFactory.connectedCal(deviceForm);
        return resp(paths);
    }

}
