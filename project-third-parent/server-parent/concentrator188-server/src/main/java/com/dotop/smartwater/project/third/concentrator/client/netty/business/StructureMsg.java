package com.dotop.smartwater.project.third.concentrator.client.netty.business;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DeviceForm;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.BinaryConversionUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.CounterUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-12 15:14
 **/
@Service
public class StructureMsg {

    @Autowired
    private CounterUtils counterUtils;

    public String makeDemo(String data) {
        String msg1 = "68";
        String len;
        String msg = data;
        len = calLen(data);
        msg = msg1 + len + len + msg1 + msg;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }


    public String makeDataInitializationMsg(String address) {
        String msg = "68710071006870";
        msg += address;
        String msg2 = "017" + counterUtils.getCounter(address) + "0000020036353433323139383736353433323130";
        msg += msg2;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeUploadStatusMsg(String address) {
        String msg = "68310031006870";
        msg += address;
        String msg2 = "8A7" + counterUtils.getCounter(address) + "00004003";
        msg += msg2;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeLoginSussessMsg(String address) {
        String msg = "68490049006810";
        msg += address;
        String msg2 = "006000000400021000010000";
        msg += msg2;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeHeartBeatSussessMsg(String address) {
        String msg = "68490049006810";
        msg += address;
        String msg2 = "006000000400021000040000";
        msg += msg2;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeReadUploadTimeMsg(String address) {
        String msg = "68310031006870";
        msg += address;
        String msg2 = "8A7" + counterUtils.getCounter(address) + "01012003";
        msg += msg2;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeUploadTimeMsg(String address, String time, String type) {
        String msg1 = "68A100A1006870" + address + "847" + counterUtils.getCounter(address) + "01012003";
        String msg3 = "000100010036353433323139383736353433323130";
        String msg = "";
        //2019-06-12 15:14:23
        String year = time.substring(2, 4);
        String month = String.valueOf(Integer.parseInt(time.substring(5, 7)) + 60);
        String date = time.substring(8, 10);
        String hour = time.substring(11, 13);
        String minutes = time.substring(14, 16);
        String second = time.substring(17, 19);

        msg = msg1 + type + second + minutes + hour + date + month + year + msg3;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeUploadTimeStatusMsg(String address, String status) {
        String msg1 = "68750075006870" + address + "847" + counterUtils.getCounter(address) + "00004003";
        String msg3 = "36353433323139383736353433323130";

        String key = "";
        if ("1".equals(status)) {
            key = "55";
        } else {
            key = "AA";
        }

        String msg = "";
        msg = msg1 + key + msg3;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    //每次只能9个
    public String makeReadFileMsg(String address, Integer count, Integer index) {
        String msg1 = "68";
        String len;
        String worked = "70" + address + "8A7" + counterUtils.getCounter(address) + "00000100";

        String _count = BinaryConversionUtils.intToHexString(count, 2).toUpperCase();
        _count = _count.substring(2, 4) + _count.substring(0, 2);
        String devno = "";
        for (int i = 1 + index; i <= count + index; i++) {
            String key = BinaryConversionUtils.intToHexString(i, 2).toUpperCase();
            devno += key.substring(2, 4) + key.substring(0, 2);
        }

        String msg;
        msg = worked + _count + devno;
        len = calLen(msg);
        msg = msg1 + len + len + msg1 + msg;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeDownloadFileMsg(String address, List<DeviceForm> list) {
        String msg1 = "68";
        String len = "";
        String worked = "70" + address + "847" + counterUtils.getCounter(address) + "00000100";

        String _count = BinaryConversionUtils.intToHexString(list.size(), 2).toUpperCase();
        _count = _count.substring(2, 4) + _count.substring(0, 2);
        String msg = "";

        for (DeviceForm d : list) {
            String num = BinaryConversionUtils.intToHexString(d.getDevnum(), 2).toUpperCase();
            num = num.substring(2, 4) + num.substring(0, 2);

            String devno = ToolUtils.leftPad(d.getDevno(), 12);
            String repeaterno = ToolUtils.leftPad(d.getRepeaterno(), 12);

            msg += num;

            //水表号
            msg += devno.substring(10, 12) + devno.substring(8, 10) + devno.substring(6, 8) + devno.substring(4, 6) +
                    devno.substring(2, 4) + devno.substring(0, 2);

            //不知道是不是固定格式
            msg += "3300001001000000";

            //中继器号
            msg += repeaterno.substring(10, 12) + repeaterno.substring(8, 10) + repeaterno.substring(6, 8) + repeaterno.substring(4, 6) +
                    repeaterno.substring(2, 4) + repeaterno.substring(0, 2);

        }

        //固定格式
        msg += "36353433323139383736353433323130";

        msg = worked + _count + msg;
        len = calLen(msg);
        msg = msg1 + len + len + msg1 + msg;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;

    }

    public String makeReadOne(String address, Integer devnum) {
        String msg = "68450045006870" + address + "8C7" + counterUtils.getCounter(address) + "00000107000100";
        String num = BinaryConversionUtils.intToHexString(devnum, 2).toUpperCase();
        num = num.substring(2, 4) + num.substring(0, 2);
        msg += num;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeReadAllMsg(String address, List<String> list) {
        String msg1 = "68";
        String len = "";
        String worked = "70" + address + "8C7" + counterUtils.getCounter(address) + "0000010700";

        String _count = BinaryConversionUtils.intToHexString(list.size(), 2).toUpperCase();
        _count = _count.substring(2, 4) + _count.substring(0, 2);
        String devno = "";
        for (String no : list) {
            String key = BinaryConversionUtils.intToHexString(Integer.parseInt(no), 2);

            devno += key.substring(2, 4) + key.substring(0, 2);
        }

        String msg = "";
        msg = worked + _count + devno;
        len = calLen(msg);
        msg = msg1 + len + len + msg1 + msg;
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeGprsInfoMsg(String address) {
        String msg = "68310031006870" + address + "8A7" + counterUtils.getCounter(address) + "00004000";
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    private String csCheck(String msg) {
        int dataLen = msg.length();

        //CS帧校验
        List<String> data = new ArrayList<>();
        for (int i = 12; i < dataLen; i += 2) {
            data.add(msg.substring(i, i + 2));
        }
        int sum = 0;
        for (String d : data) {
            sum += Integer.parseInt(d, 16);
        }
        sum %= 256;
        String cs = Integer.toHexString(sum);
        if (cs.length() % 2 == 1) {
            cs = "0" + cs;
        }
        return cs;
    }

    //计算字节长度
    private String calLen(String msg) {
        int dataLen = msg.length() * 2 + 1;
        String len = BinaryConversionUtils.intToHexString(dataLen, 2).toUpperCase();
        return len.substring(2, 4) + len.substring(0, 2);
    }


    public String makeSetGprsInfoMsg(String address, String ip, Integer port) {
        String msg = "68550155016870" + address + "847" + counterUtils.getCounter(address) + "0000400002";

        String[] iPs = ip.split("\\.");

        for (int i = 0; i < iPs.length; i++) {
            msg += BinaryConversionUtils.intToHexString(Integer.parseInt(iPs[i]), 1).toUpperCase();
        }

        String portStr = BinaryConversionUtils.intToHexString(port, 2).toUpperCase();
        portStr = portStr.substring(2, 4) + portStr.substring(0, 2);

        msg += portStr;
        msg += "C0A80001411FC0A80002421FC0A80003431F434D4E45540900000000000000000000C0A800CB591BFFFFFF0030303139323136353433323139383736353433323130";

        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeGetClockMsg(String address) {
        String msg = "68350035006870" + address + "8C7" + counterUtils.getCounter(address) + "0000010003";
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String makeSetClockMsg(String address, Date date) {
        /* 31 36 16 18 46 19*/
        String msg = "68890089006870" + address + "857" + counterUtils.getCounter(address) + "00001001";
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1 + 40;
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        msg += ToolUtils.leftPad(String.valueOf(second), 2);
        msg += ToolUtils.leftPad(String.valueOf(minute), 2);
        msg += ToolUtils.leftPad(String.valueOf(hour), 2);
        msg += ToolUtils.leftPad(String.valueOf(day), 2);
        msg += ToolUtils.leftPad(String.valueOf(month), 2);
        msg += ToolUtils.leftPad(String.valueOf(year).substring(2, 4), 2);

        msg += "36353433323139383736353433323130";
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String open(String address, Integer devnum) {
        String key = BinaryConversionUtils.intToHexString(devnum, 2);
        String no = key.substring(2, 4) + key.substring(0, 2);

        String msg = "68850085006870" + address + "857" + counterUtils.getCounter(address) + "00000100563412" +
                no + "01111111005539383736353433323130";
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

    public String close(String address, Integer devnum) {
        String key = BinaryConversionUtils.intToHexString(devnum, 2);
        String no = key.substring(2, 4) + key.substring(0, 2);

        String msg = "68850085006870" + address + "857" + counterUtils.getCounter(address) + "00000100563412" +
                no + "0111111100AA39383736353433323130";
        String cs = csCheck(msg);
        msg = msg + cs.toUpperCase() + "16";
        return msg;
    }

}
