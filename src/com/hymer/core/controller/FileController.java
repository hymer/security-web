package com.hymer.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hymer.core.BaseContoller;
import com.hymer.core.entity.FileEntity;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.FileService;
import com.hymer.core.util.FileUtils;
import com.hymer.core.util.JsonUtils;

@Controller
public class FileController extends BaseContoller {

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/file/query.ajax", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryResource(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = fileService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/file/delete.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON deleteFile(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseJSON json = new ResponseJSON();
		String id = request.getParameter("id");
		if (StringUtils.hasText(id)) {
			String file = null;
			try {
				FileEntity entity = fileService.getById(Long.parseLong(id));
				if (entity != null) {
					file = entity.getRealPath();
					fileService.delete(entity);
				}
				json.setMsg("删除成功!");
			} catch (Exception e) {
				json.setResult(false);
				json.setMsg("文件正在使用，无法删除!");
			} finally {
				FileUtils.deleteFileOrDir(file);
			}
		} else {
			json.setMsg("没有删除任何文件.");
		}
		return json;
	}

}
