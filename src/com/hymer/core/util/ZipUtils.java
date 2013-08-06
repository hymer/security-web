package com.hymer.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipUtils {

	public static void compress(String srcDir) {
		String zipFile = srcDir + "/zip.zip";
		compress(srcDir, zipFile);
	}

	public static void compress(String srcDir, String zipFilePath) {
		File srcdir = new File(srcDir);
		if (!srcdir.exists())
			throw new RuntimeException(srcDir + "不存在！");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(new File(zipFilePath));
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		// fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹
		// eg:zip.setIncludes("*.java");
		// fileSet.setExcludes(...); 排除哪些文件或文件夹
		zip.addFileset(fileSet);
		zip.execute();
	}

	private static final int BUFFER = 1024;

	@SuppressWarnings("rawtypes")
	public static void unZip(String source, String destination)
			throws Exception {
		ZipFile zipFile = new ZipFile(source);
		Enumeration emu = zipFile.getEntries();
		while (emu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) emu.nextElement();
			// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
			if (entry.isDirectory()) {
				new File(destination + entry.getName()).mkdirs();
				continue;
			}
			BufferedInputStream bis = new BufferedInputStream(
					zipFile.getInputStream(entry));
			File file = new File(destination + entry.getName());
			// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
			// 而这个文件所在的目录还没有出现过，所以要建出目录来。
			File parent = file.getParentFile();
			if (parent != null && (!parent.exists())) {
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				bos.write(data, 0, count);
			}
			bos.flush();
			bos.close();
			bis.close();
		}
		zipFile.close();
	}
}