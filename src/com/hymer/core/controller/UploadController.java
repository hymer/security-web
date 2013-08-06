package com.hymer.core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hymer.core.BaseContoller;
import com.hymer.core.ServiceException;
import com.hymer.core.entity.User;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.FileService;

/**
 * 文件上传
 * 
 * @author hymer
 * @since 2011-7-27
 */
@Controller
public class UploadController extends BaseContoller {

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/upload.ajax", method = RequestMethod.POST)
	public @ResponseBody ResponseJSON upload(HttpServletRequest request) throws ServiceException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = multipartRequest.getFileMap();
		User user = (User) request.getSession().getAttribute("user");
		fileService.saveAll(files, user);
		return new ResponseJSON("上传成功！");
	}

}
