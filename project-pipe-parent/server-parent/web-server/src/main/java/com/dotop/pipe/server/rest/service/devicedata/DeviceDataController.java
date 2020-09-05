package com.dotop.pipe.server.rest.service.devicedata;

import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.TestForm;
import com.dotop.pipe.core.utils.ExportExcelUtils;
import com.dotop.pipe.core.utils.ExportExcelVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.web.api.factory.devicedata.IDeviceDataFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @date 2019年1月16日
 */
@RestController()
@RequestMapping("/deviceData")
public class DeviceDataController implements BaseController<TestForm> {

    private final static Logger logger = LogManager.getLogger(DeviceDataController.class);

    @Value("${download.error.txt.path}")
    private String downloadBasePath;
    @Autowired
    private IDeviceFactory iDeviceFactory;
    @Autowired
    private IDeviceDataFactory deviceDataFactoryImplImport;

    @PostMapping(value = "/downText")
    public void exportExcelOne(@RequestBody String fileName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "filename=" + fileName);
        if (fileName != null) {
            // 设置文件路径
            File file = new File(downloadBasePath + fileName);
            if (file.exists()) {
                FileInputStream fileInputStream = null;
                BufferedInputStream bufferedInputStream = null;
                OutputStream outputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    bufferedInputStream = new BufferedInputStream(fileInputStream);
                    byte[] b = new byte[bufferedInputStream.available()];
                    int len = bufferedInputStream.read(b);
                    outputStream = response.getOutputStream();
                    outputStream.write(b);
                    // 人走带门
                    // bufferedInputStream.close();
                    outputStream.flush();
                } catch (Exception e) {
                    logger.error(LogMsg.to(e));
                } finally {
                    // 从外到内 关闭流
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                }
            }
        }
    }

    /**
     * 读取execl文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public String upload(@RequestParam("uploadFile") MultipartFile file) {
        String filePath = this.deviceDataFactoryImplImport.importDate(file);
        // System.out.println(filePath);
        return resp(filePath);
    }

    @PostMapping(value = "/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // String filePath = "";
        String[] otherTitle = new String[]{"设备编码", "设备名称", "设备描述", "长度", "深度", "管顶高程", "地面高程", "版本", "区域名称", "区域编号",
                "产品编码", "产品类型", "坐标编号", "经度", "纬度"};
        // String[] sensorTitle = new String[] { "设备编码", "设备名称", "设备描述", "长度", "深度",
        // "管顶高程", "地面高程", "版本", "区域名称", "区域编号",
        // "产品编码", "产品类别", "产品类型", "坐标编号", "经度", "纬度" };

        String[] pipeTitle = new String[]{"设备编码", "设备名称", "设备描述", "长度", "深度", "管顶高程", "地面高程", "版本", "区域名称", "区域编号",
                "产品编码", "产品类型", "坐标编号1", "经度1", "纬度1", "坐标编号2", "经度2", "纬度2"};

        // Map<String, String[]> titles = new HashMap<String, String[]>();
        // titles.put("pipe", pipeTitle); // 管道
        // titles.put("sensor", otherTitle); // 传感器
        // titles.put("valve", otherTitle); // 阀门
        // titles.put("node", otherTitle); // 节点
        // titles.put("plug", otherTitle); // 堵头
        // titles.put("hydrant", otherTitle); // 消防栓

        // titles.put("管道", pipeTitle); // 管道
        // titles.put("传感器", otherTitle); // 传感器
        // titles.put("阀门", otherTitle); // 阀门
        // titles.put("节点", otherTitle); // 节点
        // titles.put("堵头", otherTitle); // 堵头
        // titles.put("消防栓", otherTitle); // 消防栓

        List<ExportExcelVo> eelist = new ArrayList<>();
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_PIPE, "管道", pipeTitle)); // 管道
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_SENSOR, "计量计", otherTitle)); // 传感器
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_VALVE, "阀门", otherTitle)); // 阀门
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_NODE, "节点", otherTitle)); // 节点
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_PLUG, "堵头", otherTitle)); // 堵头
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_HYDRANT, "消防栓", otherTitle)); // 消防栓
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY, "水厂", otherTitle)); // 水厂
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_SLOPS_FACTORY, "污水厂", otherTitle)); // 污水厂
        eelist.add(new ExportExcelVo(CommonConstants.PRODUCT_CATEGORY_PUMP, "加压泵", otherTitle)); // 加压泵
        Map<String, ExportExcelVo> eeMap = eelist.stream()
                .collect(Collectors.toMap(ExportExcelVo::getCategory, a -> a, (k1, k2) -> k1));
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setLimit(10000);
        List<DeviceVo> datalist = iDeviceFactory.list(deviceForm);
        ExportExcelUtils ex = new ExportExcelUtils(eeMap, datalist);

        try { // 导出excel
            XSSFWorkbook workbook = ex.export07();
            if (workbook != null) {
                try {
                    String fileName = URLEncoder.encode("设备导出", "UTF-8") + "-"
                            + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() + ".xlsx";
                    // String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("application/vnd.ms-excel; charset=UTF-8");
                    response.setHeader("Content-Disposition", "filename=" + fileName);
                    OutputStream outputStream = response.getOutputStream();
                    workbook.write(outputStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            logger.error(LogMsg.to(e));
        }
        // return resp();
    }

}
