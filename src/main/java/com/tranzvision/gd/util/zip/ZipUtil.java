package com.tranzvision.gd.util.zip;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件目录或文件
 * @author zhanglang
 *
 */
public class ZipUtil {

	/**
	 * 复制文件
	 * @param sFile	源文件
	 * @param tFile	目标文件
	 */
	public void fileChannelCopy(String sFile, String tFile) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;

		File s = new File(sFile);
		File t = new File(tFile);
		if (s.exists() && s.isFile()) {
			try {
				fi = new FileInputStream(s);
				fo = new FileOutputStream(t);
				in = fi.getChannel();// 得到对应的文件通道
				out = fo.getChannel();// 得到对应的文件通道
				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fi.close();
					in.close();
					fo.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 创建ZIP文件
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public void createZip(ArrayList<String> sourcePathArr, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			if (sourcePathArr != null && sourcePathArr.size() > 0) {
				fos = new FileOutputStream(zipPath);
				zos = new ZipOutputStream(fos);

				for (int i = 0; i < sourcePathArr.size(); i++) {
					String sourcePath = sourcePathArr.get(i);

					writeZip(new File(sourcePath), "", zos);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				for (File f : files) {
					writeZip(f, parentPath, zos);
				}
			} else {
				FileInputStream fis = null;
				DataInputStream dis = null;
				try {
					fis = new FileInputStream(file);
					dis = new DataInputStream(new BufferedInputStream(fis));
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (dis != null) {
							dis.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
