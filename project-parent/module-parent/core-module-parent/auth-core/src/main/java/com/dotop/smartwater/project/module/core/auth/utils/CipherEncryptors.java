package com.dotop.smartwater.project.module.core.auth.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class CipherEncryptors {

    public static String encrypt(String key1, String key2, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return parseByte2HexStr(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String key1, String key2, String encrypted){
        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(parseHexStr2Byte(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public final static String cpKey1="2Qc0q9uwxuRax9Up";
	public final static String cpKey2="t4AZtaUIBultBeQz";
	
    public static void main(String[] args) {
    	System.out.println(encrypt(cpKey1, cpKey2, "哈哈哈哈好"));

        System.out.println(decrypt(cpKey1, cpKey2,
                encrypt(cpKey1, cpKey2, "哈哈哈哈好")));
    }
}