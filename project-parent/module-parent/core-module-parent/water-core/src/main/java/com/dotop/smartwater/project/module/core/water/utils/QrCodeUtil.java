package com.dotop.smartwater.project.module.core.water.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的
 * 

 * @date 2019-04-11 16:00
 * 
 */
public class QrCodeUtil {
	
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /*public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format "
                    + format + " to " + file);
        }
    }*/

    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
             * 传入二维码内容，返回生成的二维码图片流
     * @param text
     * @return
     * @throws Exception
     */
    public static byte[] createQrCodeImage(String text) throws Exception
    {
	    // text = "http://www.baidu.com"; // 二维码内容
	    int width = 240; // 二维码图片宽度
	    int height = 240; // 二维码图片高度
	    String format = "jpg";// 二维码的图片格式
	    Hashtable<EncodeHintType, String> hints = new Hashtable<>();
	    hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
	    
	    BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
	    
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    QrCodeUtil.writeToStream(bitMatrix, format, out);
	    
	    return out.toByteArray();
    }

}
