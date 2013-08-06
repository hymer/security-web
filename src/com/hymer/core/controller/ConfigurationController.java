package com.hymer.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hymer.core.BaseContoller;
import com.hymer.core.ServiceException;
import com.hymer.core.entity.Preferences;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.service.ConfigurationService;
import com.hymer.core.util.JsonUtils;

@Controller
public class ConfigurationController extends BaseContoller {

	@Autowired
	private ConfigurationService coreService;
	
	@RequestMapping(value = "/core/config/query.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON queryResource(HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getParameter("query");
		ResponseJSON json = null;
		try {
			QueryObject queryObject = null;
			queryObject = JsonUtils.fromJson(queryString, QueryObject.class);
			json = coreService.query(queryObject);
			log.info("json = " + JsonUtils.toJson(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/core/config/{id}.html", method = RequestMethod.GET)
	public @ResponseBody
	Preferences getPreferences(@PathVariable Long id) throws ServiceException {
		Preferences preference = null;
		preference = coreService.getPreferenceById(id);
		return preference;
	}

	@RequestMapping(value = "/core/config/edit.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseJSON saveOrUpdateResource(HttpServletRequest request,
			HttpServletResponse response, Preferences preferences)
			throws ServiceException {
		response.setContentType("text/html; charset=utf-8");
		ResponseJSON json = new ResponseJSON();
		Long id = preferences.getId();
		if (id == null) {
			coreService.save(preferences);
		} else {
			Preferences entity = coreService.getPreferenceById(id);
			entity.setDisabled(preferences.isDisabled());
			entity.setKey(preferences.getKey());
			entity.setValue(preferences.getValue());
			entity.setRemarks(preferences.getRemarks());
			coreService.update(entity);
		}
		json.setMsg("保存成功！");
		return json;
	}
	
}
