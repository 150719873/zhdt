package com.dotop.pipe.web.factory.emailreceiver;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.web.api.factory.emailreceiver.IEmailReceiverFactory;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.utils.EmailUtils;
import com.dotop.pipe.data.receiver.api.factory.IDeviceDataFactory;
import com.dotop.pipe.data.receiver.api.factory.IKBLReceiveFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class EmailReceiverFactoryImpl implements IEmailReceiverFactory {

    private final static Logger logger = LogManager.getLogger(EmailReceiverFactoryImpl.class);

    @Autowired
    private IKBLReceiveFactory iKBLReceiveFactory;

    @Autowired
    private IDeviceDataFactory iDeviceDataFactory;
    @Autowired
    private IDeviceService iDeviceService;

    @Value("${email.protocol}")
    private String mailProtocol;

    // 端口
    @Value("${email.port}")
    private String mailPort;

    // 邮件服务器地址
    @Value("${email.host}")
    private String mailHost;

    // 用户名称
    @Value("${email.username}")
    private String mailUserName;

    // 邮件密码
    @Value("${email.password}")
    private String mailUserPassword;

    @Value("${email.backAddress}")
    private String backAddress;

    @Value("${email.backDir}")
    private String backDir;


    @Override
    public void receive() {
        Folder folder = null;
        Store store = null;
        try {
            Session session = EmailUtils.getSession(this.mailProtocol, this.mailPort, this.mailHost);
            store = EmailUtils.getStore(session, this.mailProtocol, this.mailUserName, this.mailUserPassword);
            if (store == null) {
                logger.error("邮箱获取连接失败", store);
                return;
            }
            folder = EmailUtils.getMailFolder(store);

            /* Folder.READ_ONLY：只读权限
             * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
             */
            folder.open(Folder.READ_WRITE);    //打开收件箱
            logger.info("可读可写 模式打开收件箱", store);
            // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
            logger.info("未读邮件数: " + folder.getUnreadMessageCount());
            // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
            logger.info("删除邮件数: " + folder.getDeletedMessageCount());
            logger.info("新邮件: " + folder.getNewMessageCount());
            // 获得收件箱中的邮件总数
            logger.info("邮件总数: " + folder.getMessageCount());
            // 得到收件箱中的所有邮件,并解析
            Message[] messages = folder.getMessages();
            if (messages != null && messages.length > 0) {
                logger.info("开始解析邮件");
                parseMessage(session, messages);
            } else {
                logger.info("未找到要解析的邮件!");
            }
        } catch (MessagingException e) {
            logger.error(e);
        } catch (RuntimeException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            //释放资源
            try {
                folder.close(true);
                store.close();
            } catch (MessagingException e) {
                logger.error(e);
            }
        }
    }

    public void parseMessage(Session session, Message[] messages) throws MessagingException, IOException {
        for (int i = 0, count = messages.length; i < count; i++) {
            MimeMessage msg = (MimeMessage) messages[i];
            String deviceCode = null;
            JSONObject contentJsonObject = new JSONObject();
            try {
                // 解析邮件正文 存储为key val 值
                StringBuffer contentBbuffer = new StringBuffer(30);
                EmailUtils.getMailTextContent(msg, contentBbuffer);
                String content = contentBbuffer.toString();
                int index = content.indexOf("<html>");
                content = content.substring(0, index);
                String[] contents = content.toString().split("\r\n");
                contentJsonObject.put("enterprise_id", null);
                for (String str : contents) {
                    if (StringUtils.isNotEmpty(str)) {
                        if (str.indexOf("Instrument:") > -1) {
                            contentJsonObject.put("deviceType", str.split(" ")[3].trim());
                            continue;
                        } else if (str.indexOf("Tag:") > -1) {
                            deviceCode = str.split(":")[1].trim();
                            contentJsonObject.put("device_code", deviceCode);
                            continue;
                        } else {
                            String[] properties = str.split(":");
                            if (properties.length == 2) {
                                contentJsonObject.put(properties[0].trim(), properties[1].trim());
                            }
                        }
                    }
                }

                logger.info("设备编号: " + deviceCode);
                if (StringUtils.isEmpty(deviceCode)) {
                    EmailUtils.saveAttachment(msg, this.backDir); //保存附件
                    EmailUtils.deleteMail(msg);
                    break;
                }
                // 查看设备存不存在
                boolean cacheFlag = iDeviceDataFactory.getCacheByDeviceCode(deviceCode);
                boolean flag = true;
                DeviceBo deviceBo = new DeviceBo();
                deviceBo.setCode(deviceCode);
                // 缓存不存在
                if (!cacheFlag) {
                    flag = iDeviceService.isExist(deviceBo);
                }
                if (!flag) {
                    logger.info("保存附件");
                    EmailUtils.saveAttachment(msg, this.backDir); //保存附件
                    EmailUtils.deleteMail(msg);
                    continue;
                }
                // 添加缓存信息
                iDeviceDataFactory.setCacheByDeviceCode(deviceCode);
                boolean isContainerAttachment = EmailUtils.isContainAttachment(msg);
                // System.out.println("是否包含附件：" + isContainerAttachment);
                logger.info("是否包含附件：" + isContainerAttachment);
                if (isContainerAttachment) {
                    // 读附件内容
                    logger.info("解析附件内容并保存到数据库");
                    InputStream inputStream = EmailUtils.readMailExecl(msg);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    logger.info("邮件附件流正常");
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] strings = line.split(",");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.putAll(contentJsonObject);
                        jsonObject.put("flw_total_value", strings[5]);
                        jsonObject.put("flw_rate", strings[10]);
                        if (strings.length > 11) {
                            jsonObject.put("pressure_value", strings[12]);
                        }
                        jsonObject.put("dev_send_date", strings[1].replace("/", "-") + " " + strings[2]);
                        iKBLReceiveFactory.handle(deviceCode, jsonObject);
                    }
                } else {
                    logger.info("此邮件没有附件");
                }

                // 删除邮件
                EmailUtils.deleteMail(msg);
            } catch (Exception e) {
                logger.error(e.getMessage());
                //保存附件
                logger.info("捕获异常正在备份邮件");
                EmailUtils.saveAttachment(msg, this.backDir);
                EmailUtils.deleteMail(msg);
            }
        }
    }

    void forwardEmail(Session session, String title, Message msg, String mailUserName, String mailUserPassword, String backAddress) {
        // 发送邮件 功能屏蔽 原因端口问题
        /*try {
            // 转发邮件功能
            EmailUtils.forwardMail(session, title, msg.getContent(), msg.getContentType(), this.mailUserName, this.mailUserPassword, this.backAddress);
        } catch (IOException e) {
            logger.error("发送备份邮件异常", e);
        } catch (MessagingException e) {
            logger.error("发送备份邮件异常", e);
        }*/
    }
}
