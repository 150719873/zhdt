package com.dotop.pipe.core.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {
    // https://blog.csdn.net/qq_33945246/article/details/92760626
    /**
     * 邮件协议  使用pop3
     */
   /* private String mailProtocol;

    // 端口
    private String mailPort;

    // 邮件服务器地址
    private String mailHost;

    // 用户名称
    private String mailUserName;

    // 邮件密码
    private String mailUserPassword;

    public EmailUtils(String mailProtocol, String mailPort, String mailHost, String mailUserName, String mailUserPassword) {
        this.mailHost = mailHost;
        this.mailPort = mailPort;
        this.mailProtocol = mailProtocol;
        this.mailUserName = mailUserName;
        this.mailUserPassword = mailUserPassword;
    }*/

    /**
     * 获取会话
     *
     * @param mailProtocol
     * @param mailPort
     * @param mailHost
     * @return
     */
    public static Session getSession(String mailProtocol, String mailPort, String mailHost) {
        // 准备连接服务器的会话信息
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.host", "smtp.qq.com");
        prop.setProperty("mail.smtp.timeout", "20000");
        prop.setProperty("mail.smtp.connectiontimeout", "20000");
        prop.setProperty("mail.smtp.port", "25");//端口号
        prop.setProperty("mail.smtp.auth", "true");

        if ("imap".equals(mailProtocol)) {
            prop.setProperty("mail.store.protocol", mailProtocol);
            prop.setProperty("mail.imap.host", mailHost);
            prop.setProperty("mail.imap.port", mailPort);
        } else if ("pop3".equals(mailProtocol)) {
            prop.setProperty("mail.pop3.host", mailHost);
            prop.setProperty("mail.pop3.port", mailPort);
            prop.setProperty("mail.store.protocol", mailProtocol);
            prop.setProperty("mail.pop3.auth", "true");
        }
        // 创建Session实例对象
        Session session = Session.getInstance(prop);
        return session;
    }

    /**
     * 获取收件箱
     *
     * @param session
     * @param mailProtocol
     * @param mailUserName
     * @param mailUserPassword
     * @return
     * @throws Exception
     */
    public static Store getStore(Session session, String mailProtocol, String mailUserName, String mailUserPassword) throws Exception {
        Store store = session.getStore(mailProtocol);
        store.connect(mailUserName, mailUserPassword);
        return store;
    }

    /**
     * 获取邮件的收件箱
     *
     * @param store
     * @return
     * @throws Exception
     */
    public static Folder getMailFolder(Store store) throws Exception {
        if (store != null) {
            return store.getFolder("INBOX");
        } else {
            return null;
        }
    }

    /**
     * 获得邮件文本内容
     *
     * @param part    邮件体
     * @param content 存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }


    /**
     * 判断邮件中是否包含附件
     *
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }
                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }


    /**
     * 保存附件
     *
     * @param part    邮件中多个组合体中的其中一个组合体
     * @param destDir 附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir);
        }
    }

    /**
     * 读取输入流中的数据保存至指定目录
     *
     * @param is       输入流
     * @param fileName 文件名
     * @param destDir  文件存储目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    /**
     * 转发邮件
     *
     * @param session
     * @param message
     * @param mailUserName
     * @param mailUserPassword
     * @param backAddress
     * @throws MessagingException
     * @throws IOException
     */
    public static void forwardMail(Session session, String title, Object content, String contentType, String mailUserName, String mailUserPassword, String backAddress) throws MessagingException, IOException {
        // session.setDebug(true);
        Message forward = new MimeMessage(session);
        //forward.setSubject(message.getSubject());
        forward.setSubject(title);
        forward.setFrom(new InternetAddress(mailUserName));
        forward.setRecipient(Message.RecipientType.TO, new InternetAddress(backAddress));
        forward.setSentDate(new Date());
        forward.setContent(content, contentType);
        Transport smtp = session.getTransport("smtp");
        boolean flag = smtp.isConnected();
        if (!flag) {
            smtp.connect(mailUserName, mailUserPassword);
        }
        smtp.sendMessage(forward, forward.getAllRecipients());
        smtp.close();
    }

    public static void deleteMail(Message message) throws MessagingException {
        message.setFlag(Flags.Flag.DELETED, true);
    }

    /**
     * 读取邮件附件
     *
     * @param part
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static InputStream readMailExecl(Part part) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    return is;
                    // saveFile(is, destDir, decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    readMailExecl(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        return bodyPart.getInputStream();
                        //  saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            readMailExecl((Part) part.getContent());
        }
        return null;
    }
}