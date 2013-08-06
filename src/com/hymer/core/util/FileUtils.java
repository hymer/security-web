package com.hymer.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.hymer.core.Configuration;
import com.hymer.core.entity.FileEntity;

public class FileUtils {

	public static final String SQPARATOR = "/";

	public static FileEntity writeFile(MultipartFile multipartFile) {
		String rootFolder = Configuration.get("upload.path");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		String dateFolder = year + SQPARATOR + month + SQPARATOR + day
				+ SQPARATOR;

		String folder = rootFolder + SQPARATOR + dateFolder;

		String originalFilename = multipartFile.getOriginalFilename();
		String contentType = multipartFile.getContentType();
		long size = multipartFile.getSize();

		String fileType = originalFilename.substring(originalFilename
				.lastIndexOf("."));
		String fileName = System.currentTimeMillis() + fileType;
		File writeToFolder = new File(folder);
		if (!writeToFolder.exists()) {
			writeToFolder.mkdirs();
		}
		try {
			File newFile = new File(folder + fileName);
			OutputStream os = new FileOutputStream(newFile);
			byte[] datas = multipartFile.getBytes();
			os.write(datas);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileEntity entity = new FileEntity();
		entity.setCreateTime(new Date());
		entity.setFileName(originalFilename);
		entity.setFileType(fileType);
		entity.setRealPath(folder + fileName);
		entity.setSize(size);
		entity.setContentType(contentType);
		return entity;
	}

	/*
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath String 原文件路径 如：c:/old
	 * 
	 * @param newPath String 复制后路径 如：f:/new
	 * 
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}

	public static void deleteDir(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i]);
				}
			}
			file.delete();
		} else {
			System.out.println("待删除的文件[" + file.getName() + "]不存在！"
					+ '\n');
		}
	}

}
