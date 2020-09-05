package com.dotop.smartwater.project.module.client.third;

/**

 */
public interface IOssService {

	/**
	 * 在OssPathCode有常量定义，不懂请查看
	 *
	 * @param array       上传的对象字节
	 * @param type        上传的类型
	 * @param model       对象所属模块
	 * @param filename    文件名(请传入MD5文件名)
	 * @param contentType 文件类型 例如 image/png 这种
	 * @param userId      用户ID
	 * @return 文件路径链接地址
	 */
	String upLoad(byte[] array, String type, String model, String filename, String contentType, String userId);
	
	/**
	 * 在OssPathCode有常量定义，不懂请查看
	 *
	 * @param array       上传的对象字节
	 * @param type        上传的类型
	 * @param model       对象所属模块
	 * @param filename    文件名(请传入MD5文件名)
	 * @param contentType 文件类型 例如 image/png 这种
	 * @return 文件路径链接地址
	 */
	String upLoad(byte[] array, String type, String model, String filename, String contentType);


	/**
	 * 在OssPathCode有常量定义，不懂请查看
	 *
	 * @param array       上传的对象字节
	 * @param type        上传的类型
	 * @param model       对象所属模块
	 * @param filename    文件名(请传入MD5文件名)
	 * @param contentType 文件类型 例如 image/png 这种
	 * @return 文件路径链接地址, 其中链接返回文件名, 用#分割
	 */
	String upLoadV2(byte[] array, String type, String model, String filename, String contentType);

	/**
	 * 删除文件
	 *
	 * @param type
	 * @param model
	 * @param filename
	 */
	void del(String type, String model, String filename);


	/**
	 * 返回配置好的OSS域名
	 *
	 * @return
	 */
	String getOssPrefix();


	/**
	 * 获取文件类型
	 *
	 * @param filename
	 * @return
	 */
	String getContentType(String filename);
	
	/**
	 * 获取图片流
	 * @param path
	 * @return
	 */
	byte[] getImgByte(String path);

}
