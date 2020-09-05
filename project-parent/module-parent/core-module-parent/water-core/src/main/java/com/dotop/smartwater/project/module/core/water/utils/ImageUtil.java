package com.dotop.smartwater.project.module.core.water.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static BufferedImage resizePng(byte[] fromFile, int outputWidth, int outputHeight, boolean proportion) {
		try {

			ByteArrayInputStream imageStream = new ByteArrayInputStream(fromFile);
			BufferedImage bi2 = ImageIO.read(imageStream);

			int newWidth;
			int newHeight;
			// 判断是否是等比缩放
			if (proportion) {
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
				double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
				// 根据缩放比率大的进行缩放控制
				double rate = rate1 < rate2 ? rate1 : rate2;
				newWidth = (int) (((double) bi2.getWidth(null)) / rate);
				newHeight = (int) (((double) bi2.getHeight(null)) / rate);
			} else {
				newWidth = outputWidth; // 输出的图片宽度
				newHeight = outputHeight; // 输出的图片高度
			}

			BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = to.createGraphics();
			to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);
			g2d.dispose();

			// 消去PNG透明底图片颜色变红
			g2d = to.createGraphics();
			@SuppressWarnings("static-access")
			Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
			g2d.drawImage(from, 0, 0, null);
			g2d.dispose();
			return to;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedImage resizeJPG(byte[] fromFile, int outputWidth, int outputHeight, boolean proportion) {
		try {
			ByteArrayInputStream imageStream = new ByteArrayInputStream(fromFile);
			BufferedImage bi2 = ImageIO.read(imageStream);

			double wr = 0, hr = 0;

			// 设置缩放目标图片模板
			Image Itemp = bi2.getScaledInstance(outputWidth, outputHeight, Image.SCALE_SMOOTH);

			// 获取缩放比例
			wr = outputWidth * 1.0 / bi2.getWidth();
			hr = outputHeight * 1.0 / bi2.getHeight();

			AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
			Itemp = ato.filter(bi2, null);
			return (BufferedImage) Itemp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
