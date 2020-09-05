package com.dotop.smartwater.project.module.core.water.utils;

import java.io.*;

public class FileUtil {

	public static boolean deleteFiles(File file, int min) {
		boolean flag = true;
		long time = System.currentTimeMillis();
		if (min > 0) {
			time = time - min * 60 * 1000;
		}
		if (file != null && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					long filetime = f.lastModified();
					if (filetime < time) {
						flag = f.delete();
					}
				}
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;

	}


	/**
	 * 读取源文件内容
	 *
	 * @param filename String 文件路径
	 * @return byte[] 文件内容
	 * @throws IOException
	 */
	public static byte[] readFile(String filename) throws IOException {
		if (filename == null || "".equals(filename)) {
			throw new IllegalArgumentException("无效的文件路径");
		}
		File file = new File(filename);
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
			long len = file.length();
			byte[] bytes = new byte[(int) len];
			int r = bufferedInputStream.read(bytes);
			if (r != len) {
				throw new IllegalArgumentException("读取文件不正确");
			}
			return bytes;
		}
	}

}
