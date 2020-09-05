package com.dotop.smartwater.project.third.meterread.webservice.client.fegin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteCustomerBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 180600809
 * <p>
 * 39003a0038
 */
public final class CxfClient {

    private static final Logger LOGGER = LogManager.getLogger(CxfClient.class);

    // TODO
    private final static String url = "http://114.115.147.198:82/webServicePF/services/ycService?wsdl";
    private final static boolean debug = false;
//    private final static String url = "";
//    private final static boolean debug = true;

    public static void custinfo(List<RemoteCustomerBo> list) throws FrameworkRuntimeException {
        if (list.isEmpty()) {
            return;
        }
        JSONArray json = new JSONArray();
        JSONObject temp = null;
        for (int i = 0; i < list.size(); i++) {
            RemoteCustomerBo rc = list.get(i);
            temp = new JSONObject();
            temp.put("factoryid", rc.getFactoryId());
            temp.put("meteraddr", rc.getMeterAddr());
            temp.put("username", rc.getUserName());
            temp.put("linkman", rc.getLinkman());
            temp.put("phone", rc.getPhone());
            temp.put("paperno", rc.getPaperNo());
            temp.put("address", rc.getAddress());
            temp.put("caliber", rc.getCaliber());
            temp.put("installdate", DateUtils.formatDatetime(rc.getInstallDate()));
            temp.put("ifctrlvalve", rc.getIfCtrlValve());
            // temp.put("remark", rc.getRemark());
            temp.put("usercode", rc.getUserCode());
            temp.put("extenddata1", rc.getExtendData1());
            temp.put("extenddata2", rc.getExtendData2());
            temp.put("extenddata3", rc.getExtendData3());
            temp.put("latestnumber", rc.getLatestNumber());
            json.add(temp);
        }
        System.out.println(json);
        if (debug) {
            return;
        }
        LOGGER.warn(LogMsg.to("json", json));
        Object[] objects = null;
        try {
            // 创建动态客户端
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(url);
            objects = client.invoke("custinfo", new Object[]{"1", json.toString()});
            Object result = objects[0];
            LOGGER.warn(LogMsg.to("返回数据", result));
            if (!result.equals("100")) {
                LOGGER.warn(LogMsg.to("返回数据", objects));
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED);
            }
        } catch (Exception e) {
            LOGGER.warn(LogMsg.to("返回数据", objects));
            LOGGER.warn(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED, e);
        }
    }

    public static void custinfoRqmd(List<RemoteCustomerBo> list) throws FrameworkRuntimeException {
        if (list.isEmpty()) {
            return;
        }
        JSONArray json = new JSONArray();
        JSONObject temp = null;
        for (int i = 0; i < list.size(); i++) {
            RemoteCustomerBo rc = list.get(i);
            temp = new JSONObject();
            temp.put("factoryid", rc.getFactoryId());
            temp.put("meteraddr", rc.getMeterAddr());
            temp.put("orldmeteraddr", rc.getOldMeterAddr());
            temp.put("username", rc.getUserName());
            temp.put("linkman", rc.getLinkman());
            temp.put("phone", rc.getPhone());
            temp.put("paperno", rc.getPaperNo());
            temp.put("address", rc.getAddress());
            temp.put("caliber", rc.getCaliber());
            temp.put("installdate", DateUtils.formatDatetime(rc.getInstallDate()));
            temp.put("ifctrlvalve", rc.getIfCtrlValve());
            // temp.put("remark", rc.getRemark());
            temp.put("usercode", rc.getUserCode());
            temp.put("extenddata1", rc.getExtendData1());
            temp.put("extenddata2", rc.getExtendData2());
            temp.put("extenddata3", rc.getExtendData3());
            temp.put("wateyield", rc.getWateyield());
            temp.put("latestnumber", rc.getLatestNumber());
            json.add(temp);
        }
        System.out.println(json);
        if (debug) {
            return;
        }
        LOGGER.warn(LogMsg.to("json", json));
        Object[] objects = null;
        try {
            // 创建动态客户端
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(url);
            objects = client.invoke("custinfoRqmd", new Object[]{"1", json.toString()});
            Object result = objects[0];
            LOGGER.warn(LogMsg.to("返回数据", result));
            if (!result.equals("100")) {
                LOGGER.warn(LogMsg.to("返回数据", objects));
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED);
            }
        } catch (Exception e) {
            LOGGER.warn(LogMsg.to("返回数据", objects));
            LOGGER.warn(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED, e);
        }
    }


    public static void remoteData(List<RemoteDataBo> list) throws FrameworkRuntimeException {
        if (list.isEmpty()) {
            return;
        }
        JSONArray json = new JSONArray();
        JSONObject temp = null;
        for (int i = 0; i < list.size(); i++) {
            RemoteDataBo rd = list.get(i);
            temp = new JSONObject();
            temp.put("factoryid", rd.getFactoryId());
            temp.put("meteraddr", rd.getMeterAddr());
            temp.put("readnumber", rd.getReadNumber());
            temp.put("readdate", DateUtils.formatDatetime(rd.getReadDate()));
            json.add(temp);
        }
        System.out.println(json);
        if (debug) {
            return;
        }
        LOGGER.warn(LogMsg.to("json", json));
        Object[] objects = null;
        try {
            // 创建动态客户端
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(url);
            objects = client.invoke("remoteData", new Object[]{"1", json.toString()});
            Object result = objects[0];
            LOGGER.warn(LogMsg.to("返回数据", result));
            if (!result.equals("100")) {
                LOGGER.warn(LogMsg.to("返回数据", objects));
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED);
            }
        } catch (Exception e) {
            LOGGER.warn(LogMsg.to("返回数据", objects));
            LOGGER.warn(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_FAILED, e);
        }
    }
}

