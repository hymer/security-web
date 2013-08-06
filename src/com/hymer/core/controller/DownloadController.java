package com.hymer.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hymer.core.BaseContoller;
import com.hymer.core.entity.FileEntity;
import com.hymer.core.service.FileService;

@Controller
public class DownloadController extends BaseContoller {

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/download/{id}.html", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @PathVariable Long id) {
		FileEntity fileEntity = fileService.getById(id);
		response.setCharacterEncoding("utf-8");
		response.setContentType(fileEntity.getContentType());
		try {
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(fileEntity.getFileName().getBytes("utf-8"),
							"ISO8859-1"));
			File file = new File(fileEntity.getRealPath());
			InputStream inputStream = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[1024];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			inputStream.close();
			os.flush();
			os.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
