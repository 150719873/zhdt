package com.dotop.smartwater.project.third.module.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.third.nb.vo.DeviceCurrentDataVo;
import com.dotop.smartwater.project.third.module.core.third.nb.vo.DeviceInfoVo;
import com.dotop.smartwater.project.third.module.core.utils.webservice.WebServiceUtils;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class Nb1Utils {

    private static final Logger LOGGER = LogManager.getLogger(Nb1Utils.class);

    private static final String HASH_ALGORITHM = "SHA1";
    private String shareKey;
    private byte[] byKey;
    private byte[] byIV;
    private static final Integer PAGESIZE = 5000;
    private static final String YYYYMMDD = "yyyyMMdd";
    private static final String AGREEMENT = "agreement";
    private static final String WATER = "water";
    private static final String TAPSTATUS = "tapStatus";
    private static final String DELIVERY = "delivery";
    private static final String TAPTYPE = "taptype";
    private static final Integer RESULT_CODE_SUCCESS = 1;
    private static final Integer RESULT_CODE_FAIL = 0;

    public Nb1Utils() throws Exception {
        this.setShareKey(this.generateNewShareKey());
    }

    public Nb1Utils(String shareKey) throws Exception {
        setShareKey(shareKey);
    }

    /**
     * 生成密钥
     *
     * @return
     */
    private byte[] generateKey() {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 生成一个DES算法的KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(sr);
            // 生成密钥
            SecretKey secretKey = kg.generateKey();
            // 获取密钥数据
            byte[] key = secretKey.getEncoded();
            return key;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("DES算法，生成密钥出错!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成新共享密钥
     *
     * @return
     */
    private String generateNewShareKey() {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            return base64en.encode(generateKey());
        } finally {
            base64en = null;
        }
    }

    /**
     * 加密函数
     *
     * @param data
     * @return
     */
    private String encrypt(String data) {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(byKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data.getBytes("UTF8"));
            return base64en.encode(encryptedData);
        } catch (Exception e) {
            System.err.println("DES算法，加密数据出错!");
            e.printStackTrace();
        } finally {
            base64en = null;
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param data
     * @return
     */
    private String decrypt(String data) {
        BASE64Decoder base64de = new BASE64Decoder();
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // byte rawKeyData[] = /* 用某种方法获取原始密匙数据 */;
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(byKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(base64de.decodeBuffer(data));
            return new String(decryptedData, "UTF8");
        } catch (Exception e) {
            System.err.println("DES算法，解密出错。");
            e.printStackTrace();
        } finally {
            base64de = null;
        }
        return null;
    }

    /**
     * CBC加密函数
     *
     * @param data
     * @return
     */
    public String CBCEncrypt(String data) {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(byKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            // 若采用NoPadding模式，data长度必须是8的倍数
            // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            // 用密匙初始化Cipher对象
            IvParameterSpec param = new IvParameterSpec(byIV);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);
            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(data.getBytes("UTF8"));
            return base64en.encode(encryptedData);
        } catch (Exception e) {
            System.err.println("DES CBC算法，加密数据出错!");
            e.printStackTrace();
        } finally {
            base64en = null;
        }
        return null;
    }

    /**
     * CBC解密函数
     *
     * @param data
     * @return
     */
    private String CBCDecrypt(String data) {
        BASE64Decoder base64de = new BASE64Decoder();
        try {
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(byKey);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            // using DES in CBC mode
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            // 若采用NoPadding模式，data长度必须是8的倍数
            // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            // 用密匙初始化Cipher对象
            IvParameterSpec param = new IvParameterSpec(byIV);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, param);
            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(base64de.decodeBuffer(data));
            return new String(decryptedData, "UTF8");
        } catch (Exception e) {
            System.err.println("DES CBC算法，解密出错。");
            e.printStackTrace();
        } finally {
            base64de = null;
        }
        return null;
    }

    /**
     * 哈希(SHA1)并CBC加密
     *
     * @param data
     * @return
     */
    private String HashAndEncode(String data) {
        MessageDigest md = null;
        String strDes = null;

        try {
            byte[] bt = data.getBytes("UTF8");
            md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(bt);
            strDes = CBCEncrypt(bytes2Hex(md.digest())); // to HexString
        } catch (Exception e) {
            System.err.println("散列值，加密出错。");
            return null;
        }
        return strDes;
    }

    private String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF)).toUpperCase();
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    private void getKeyAndIV() {
        String KEY_64 = shareKey;
        String IV_64 = shareKey.substring(3) + shareKey.substring(0, 3);
        byte[] byKey0;
        byte[] byIV0;
        try {
            byKey0 = KEY_64.getBytes("UTF8");
            byIV0 = IV_64.getBytes("UTF8");
            byKey = fixEightBytes(byKey0);
            byIV = fixEightBytes(byIV0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private byte[] fixEightBytes(byte[] bytes) {
        if (bytes.length == 8) {
            return bytes;
        }
        byte[] tmpBytes = new byte[8];
        int j = 0;
        for (int i = 0; i < 8; i++) {
            if (j >= bytes.length) {
                j = 0;
            }
            tmpBytes[i] = bytes[j++];
        }
        return tmpBytes;
    }

    private String getShareKey() {
        return shareKey;
    }

    private void setShareKey(String shareKey) throws Exception {
        if (shareKey == null || shareKey.length() < 4) {
            throw new Exception("共享密钥必须4字符以上！");
        }
        this.shareKey = shareKey;
        getKeyAndIV();
    }

    private static JSONObject analyse(String returnCommand) {
        JSONObject jsonObject = new JSONObject();
        // 默认NBh协议
        try {
            JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(returnCommand).getString("dev"));
            jsonArray.forEach(object -> {
                JSONObject temp = JSONObject.parseObject(object.toString());
                switch (temp.getString("bn")) {
                    case "/3/0":
                        String agreement = temp.getString("1");
                        if (agreement == null) {
                            agreement = temp.getString("Model");
                        }
                        jsonObject.put(AGREEMENT, agreement);
                        break;
                    case "/80/0":
                        Double water = temp.getDouble("16");
                        if (water == null) {
                            water = temp.getDouble("Reading");
                        }
                        jsonObject.put(WATER, water);
                        break;
                    case "/81/0":
                        // 阀门状态 R Single Optional Integer 0 - 开阀状态 1 - 关阀状态
                        Integer tapStatus = temp.getInteger("1");
                        if (tapStatus == null) {
                            String tapStatusStr = temp.getString("Valve Status");
                            if ("Open".equalsIgnoreCase(tapStatusStr)) {
                                tapStatus = 1;
                            } else {
                                tapStatus = 0;
                            }
                        } else if (tapStatus == 1) {
                            // 关阀
                            tapStatus = 0;
                        } else if (tapStatus == 0) {
                            // 开阀
                            tapStatus = 1;
                        } else {
                            // null
                            tapStatus = 1;
                        }
                        jsonObject.put(TAPSTATUS, tapStatus);
                        Integer taptype = temp.getInteger("3");
                        if (taptype == null) {
                            String taptypeStr = temp.getString("ValveType");
                            if ("Five lines".equalsIgnoreCase(taptypeStr) || "Two lines".equalsIgnoreCase(taptypeStr)) {
                                taptype = 1;
                            } else {
                                taptype = 0;
                            }
                        } else if (taptype != 2) {
                            // 带阀
                            taptype = 1;
                        } else {
                            // 无阀
                            taptype = 0;
                        }
                        jsonObject.put(TAPTYPE, taptype);
                        break;
                    case "/84/0":
                        Integer delivery = temp.getInteger("0");
                        if (delivery == null) {
                            delivery = temp.getInteger("ReportCycle");
                        }
                        jsonObject.put(DELIVERY, delivery);
                        break;
                    default:
                        break;
                }
            });
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
        // TODO 如果不是NBh此处加
        return jsonObject;
    }

    private static Object webservice(DockingForm dockingFrom, DockingForm loginDocking, Object[] search) throws FrameworkRuntimeException {
        try {
            String userName = loginDocking.getUsername() + "";
            String password = loginDocking.getPassword() + "";
            if (userName.length() < 4) {
                userName = userName + "1";
            }
            Nb1Utils nb1Utils = new Nb1Utils(userName);
            String mdPassword = nb1Utils.CBCEncrypt(password);
            Object[] append = new Object[]{loginDocking.getUsername(), mdPassword};
            Object[] all = new Object[append.length + search.length];
            System.arraycopy(append, 0, all, 0, append.length);
            System.arraycopy(search, 0, all, append.length, search.length);
            Object[] objects = WebServiceUtils.client(dockingFrom.getHost(), dockingFrom.getUrl(), Arrays.asList(all));
            JSONObject jsonObject = JSON.parseObject(objects[0].toString());
            Integer resultCode = jsonObject.getInteger("resultCode");
            String resultMessage = jsonObject.getString("resultMessage");
            LOGGER.info(LogMsg.to("resultCode", resultCode, "resultMessage", resultMessage));
            if (!RESULT_CODE_SUCCESS.equals(resultCode)) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "获取失败");
            }
            return objects[0];
        } catch (Exception e) {
            LOGGER.error(e);
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage());
        }
    }

    private static JSONArray getList(DockingForm dockingFrom, DockingForm loginDocking, Object[] objects, Integer pageIndex) {
        try {
            Object object = Nb1Utils.webservice(dockingFrom, loginDocking, objects);
            JSONObject jsonObject = JSON.parseObject(object.toString());
            Integer total = Integer.parseInt(jsonObject.getString("total"));
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (total > PAGESIZE) {
                for (int i = 1; total > PAGESIZE * i; i++) {
                    objects[pageIndex] = i + 1;
                    Object object1 = Nb1Utils.webservice(dockingFrom, loginDocking, objects);
                    JSONObject result = JSON.parseObject(object1.toString());
                    JSONArray resultJSONArray = result.getJSONArray("data");
                    jsonArray.addAll(resultJSONArray);
                }
            }
            return jsonArray;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage());
        }
    }

    static List<String> passEuis;

    static {
        // TODO 异常表号
        String[] ary = {"860705048075033", "860705047854289", "860705048074184", "860705047736213", "860705047848612", "860705044269499"};
        passEuis = Arrays.asList(ary);
    }

    public static List<DeviceForm> getDeviceInfo(DockingForm dockingFrom, DockingForm loginDocking) {
        Date now = new Date();
        List<DeviceForm> deviceFroms = new ArrayList<>();
        Object[] objects = new Object[5];
        objects[0] = DateUtils.format(DateUtils.month(now, -10), YYYYMMDD);
        objects[1] = DateUtils.format(now, YYYYMMDD);
        objects[2] = "";
        objects[3] = PAGESIZE;
        objects[4] = 1;
        // TODO 暂时用于测试，的日期
//        objects[0] = "20190401";
//        objects[1] = "20190801";
        JSONArray jsonArray = getList(dockingFrom, loginDocking, objects, 4);
        for (Object o : jsonArray) {
            DeviceInfoVo deviceInfoVo = BeanUtils.copy(o, DeviceInfoVo.class);
            if (passEuis.contains(deviceInfoVo.getImei())) {
                LOGGER.warn(LogMsg.to("msg", "跳过imei", "deviceInfoVo", deviceInfoVo));
            }
            if (deviceInfoVo.getDeviceId().length() == 12) {
                DeviceForm deviceFrom = new DeviceForm();
                deviceFrom.setId(UuidUtils.getUuid());
                deviceFrom.setDeveui(deviceInfoVo.getImei());
                deviceFrom.setEnterpriseid(loginDocking.getEnterpriseid());
                deviceFrom.setDevid(UuidUtils.getUuid());
//            deviceFrom.setDevno(deviceInfoVo.getName());
                deviceFrom.setDevno(deviceInfoVo.getDeviceId());
                deviceFrom.setJson(o.toString());
                deviceFrom.setThirdid("");
                deviceFrom.setMode(loginDocking.getMode());
                deviceFrom.setProductName(loginDocking.getProductName());
                deviceFrom.setFactory(loginDocking.getFactory());
                deviceFrom.setCaliber("");
                deviceFroms.add(deviceFrom);
            } else {
                LOGGER.warn(LogMsg.to("msg", "跳过", "deviceInfoVo", deviceInfoVo));
            }
        }
        LOGGER.info(LogMsg.to("deviceFroms", deviceFroms));
        return deviceFroms;
    }

    public static List<DeviceUplinkForm> getDeviceCurrentData(DockingForm dockingFrom, DockingForm loginDocking) {
        List<DeviceUplinkForm> deviceUplinkFroms = new ArrayList<>();
        Object[] objects = new Object[3];
        objects[0] = "";
        objects[1] = PAGESIZE;
        objects[2] = 1;
        JSONArray jsonArray = getList(dockingFrom, loginDocking, objects, 2);
//        LOGGER.warn(LogMsg.to("jsonArray", jsonArray));
        jsonArray.forEach((o) -> {
            DeviceCurrentDataVo deviceCurrentDataVo = null;
            try {
                deviceCurrentDataVo = BeanUtils.copy(o, DeviceCurrentDataVo.class);
//                if (deviceCurrentDataVo.getImei().equals("860819042971517")) {
//                    System.out.println("860819042971517");
//                }
                if (passEuis.contains(deviceCurrentDataVo.getImei())) {
                    LOGGER.warn(LogMsg.to("msg", "跳过2imei", "deviceCurrentDataVo", deviceCurrentDataVo));
                }
                if (deviceCurrentDataVo.getDeviceId().length() == 12) {
                    DeviceUplinkForm deviceUplinkFrom = new DeviceUplinkForm();
                    deviceUplinkFrom.setId(UuidUtils.getUuid());
                    deviceUplinkFrom.setDeveui(deviceCurrentDataVo.getImei());
                    deviceUplinkFrom.setDevid("");
                    deviceUplinkFrom.setThirdid("");
                    deviceUplinkFrom.setUplinkDate(DateUtils.parse(deviceCurrentDataVo.getReciveTime(), DateUtils.YYYYMMDDHHMMSS));
                    deviceUplinkFrom.setEnterpriseid(loginDocking.getEnterpriseid());
                    deviceUplinkFrom.setJson(o.toString());
                    JSONObject analyse = analyse(deviceCurrentDataVo.getReturnCommand());
                    BigDecimal b = BigDecimal.valueOf(analyse.getDouble(WATER) / 1000);
                    Double water = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                    deviceUplinkFrom.setWater(water);
                    deviceUplinkFrom.setAgreement(analyse.getString(AGREEMENT));
                    deviceUplinkFrom.setTapstatus(Integer.valueOf(analyse.getString(TAPSTATUS)));// TODO 1
                    deviceUplinkFrom.setDelivery(analyse.getInteger(DELIVERY));
                    deviceUplinkFrom.setTaptype(Integer.valueOf(analyse.getString(TAPTYPE)));// TODO 0

                    deviceUplinkFroms.add(deviceUplinkFrom);
                } else {
                    LOGGER.warn(LogMsg.to("msg", "跳过2", "deviceCurrentDataVo", deviceCurrentDataVo));
                }
//                LOGGER.info(LogMsg.to("o", o, "deviceCurrentDataVo", deviceCurrentDataVo));
            } catch (Exception e) {
                LOGGER.error(LogMsg.to("o", o, "deviceCurrentDataVo", deviceCurrentDataVo));
                LOGGER.error(e);
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
            }
        });
        return deviceUplinkFroms;
    }

//    public static List<DeviceUplinkForm> getDeviceFreezeData(DockingForm dockingFrom) {
//        dockingFrom.setUrl("getDeviceFreezeData");
//        List<DeviceUplinkForm> deviceUplinkFroms = new ArrayList<>();
//        Object[] objects = new Object[5];
//        objects[0] = "";
//        objects[1] = "20191009";
//        objects[2] = "20191009";
//        objects[3] = PAGESIZE;
//        objects[4] = 1;
//        JSONArray jsonArray = getList(dockingFrom, objects, 4);
//        return deviceUplinkFroms;
//    }


    public static String sendCommand(DockingForm dockingFrom, DockingForm loginDocking, CommandForm commandForm) {
        Object[] objects = new Object[3];
        objects[0] = commandForm.getDeveui();
        objects[1] = 0;
        objects[2] = "";
        try {
            switch (commandForm.getCommand()) {
                case TxCode.CloseCommand:
                    objects[1] = 1;
                    objects[2] = "1";
                    //关阀
                    break;
                case TxCode.OpenCommand:
                    objects[1] = 1;
                    objects[2] = "0";
                    //开阀
                    break;
                case TxCode.GetWaterCommand:
                    //实时抄表
                    break;
                case TxCode.MeterOper:
                    //水表调整
                    break;
                case TxCode.Reset:
                    //水表复位
                    break;
                case TxCode.SetLifeStatus:
                    //设置生命周期
                    break;
                case TxCode.ResetPeriod:
                    //设置定时重启周期
                    break;
                case TxCode.SetUploadTime:
                    objects[1] = 3;
                    objects[2] = "0";
                    //设置上报周期
                    break;
                default:
                    throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "未知指令");
            }
//            Object object = webservice(dockingFrom, loginDocking, objects);
//            JSONObject jsonObject = JSON.parseObject(object.toString());
//            String resultCode = jsonObject.getString("resultCode");
//            if (!RESULT_SUCCESS.equals(resultCode)) {
//                throw new FrameworkRuntimeException("下发命令失败");
//            }
            return DockingConstants.RESULT_SUCCESS;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm", commandForm, "发送命令失败"));
            return DockingConstants.RESULT_FAIL;
        }
    }

    public static String cancelCommand(DockingForm dockingFrom, DockingForm loginDocking, CommandForm commandForm) {
        try {
            Object[] objects = new Object[3];
            objects[0] = commandForm.getDeveui();
            objects[1] = 1;
            Object object = webservice(dockingFrom, loginDocking, objects);
            JSONObject jsonObject = JSON.parseObject(object.toString());
            String resultCode = jsonObject.getString("resultCode");
            String resultMessage = jsonObject.getString("resultMessage");
            LOGGER.info(LogMsg.to("resultCode", resultCode, "resultMessage", resultMessage));
            if (!DockingConstants.RESULT_SUCCESS.equals(resultCode)) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "取消命令失败");
            }
            return DockingConstants.RESULT_SUCCESS;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm", commandForm, "取消命令失败"));
            return DockingConstants.RESULT_FAIL;
        }
    }

    public static CommandVo getCommandStatus(DockingForm dockingFrom, DockingForm loginDocking, CommandForm commandForm) {
        CommandVo commandVo = BeanUtils.copy(commandForm, CommandVo.class);
        try {
            Object[] objects = new Object[3];
            objects[0] = commandForm.getDeveui();
            objects[1] = 1;
            Object object = webservice(dockingFrom, loginDocking, objects);
            JSONObject jsonObject = JSON.parseObject(object.toString());
            String resultCode = jsonObject.getString("resultCode");
            String resultMessage = jsonObject.getString("resultMessage");
            LOGGER.info(LogMsg.to("resultCode", resultCode, "resultMessage", resultMessage));
            if (!DockingConstants.RESULT_SUCCESS.equals(resultCode)) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "查询命令失败");
            }
//            commandVo.setStatus(DockingConstants.COMMAND_FINISH);
//            commandVo.setResult(DockingConstants.RESULT_SUCCESS);
//            commandVo.setDes(DockingConstants.RESULT_SUCCESS_MSG);
            return commandVo;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm", commandForm, "查询命令失败"));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e.getMessage(), e);
        }
    }
}
