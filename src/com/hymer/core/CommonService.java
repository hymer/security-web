package com.hymer.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hymer.core.model.PageInfo;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.model.SortInfo;

@Transactional
public abstract class CommonService {
	protected Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ICommonDAO commonDAO;

	public <T extends BaseEntity> T save(T model) {
		return commonDAO.save(model);
	}

	public <T extends BaseEntity> void saveOrUpdate(T model) {
		commonDAO.saveOrUpdate(model);
	}

	public <T extends BaseEntity> void update(T model) {
		commonDAO.update(model);
	}

	public <T extends BaseEntity> void delete(T model) {
		commonDAO.delete(model);
	}
	
	protected ResponseJSON getBlankResponseJSON() {
		ResponseJSON json = new ResponseJSON();
		json.put("data", new Object[] {});
		json.put("pageinfo", new PageInfo());
		json.put("sortinfo", new SortInfo());
		return json;
	}
}
