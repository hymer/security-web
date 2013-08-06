package com.hymer.core.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hymer.core.BaseContoller;
import com.hymer.core.entity.FileEntity;
import com.hymer.core.util.FileUtils;
import com.hymer.core.util.ZipUtils;

@Controller
public class SystemUpdater extends BaseContoller {

	@RequestMapping(value = "/updater.html", method = RequestMethod.POST)
	public synchronized ModelAndView init(HttpServletRequest request) {
		ModelAndView mv = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("new_version_package");
			if (null == file) {
				throw new RuntimeException("没有找到升级包，请上传升级包");
			}
			String rootPath = request.getServletContext().getRealPath("/");
			FileEntity entity = FileUtils.writeFile(file);
			String zipFilePath = entity.getRealPath();
			File webRoot = new File(rootPath);
			String backUpZip = webRoot.getParent() + "/ROOT.bak.zip";
			File backUpFile = new File(backUpZip);
			if (backUpFile.exists()) {
				backUpFile.delete();
			}
			ZipUtils.compress(rootPath, backUpZip);
			FileUtils.deleteDir(webRoot);
			ZipUtils.unZip(zipFilePath, rootPath);
			
			mv = getSuccessMV(request);
			mv.addObject(BaseContoller.COMMON_MESSAGE_KEY, "系统升级成功!请重新登录系统。如在使用过程中遇到问题，请重启应用或手动升级。");
			mv.addObject(BaseContoller.COMMON_URL_KEY, "login.jsp");
			mv.addObject(BaseContoller.COMMON_URL_DISPLAY_KEY, "重新登录");
			mv.addObject(BaseContoller.COMMON_AUTO_REDIRECT, false);
		} catch (Exception e) {
			mv = getFailureMV(request);
			mv.addObject(BaseContoller.COMMON_MESSAGE_KEY, e.getMessage());
		}
		return mv;
	}
	
}
