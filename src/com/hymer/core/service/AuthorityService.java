package com.hymer.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hymer.core.CommonService;
import com.hymer.core.dao.AuthorityDAO;
import com.hymer.core.dao.ResourceDAO;
import com.hymer.core.entity.Authority;
import com.hymer.core.entity.Resource;
import com.hymer.core.model.Condition;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@Service
public class AuthorityService extends CommonService {

	@Autowired
	private AuthorityDAO authorityDAO;
	@Autowired
	private ResourceDAO resourceDAO;

	public ResponseJSON query(QueryObject queryObject) {
		return authorityDAO.getAll(queryObject);
	}

	public Authority getAuthorityById(Long id) {
		return authorityDAO.getById(id);
	}

	public void saveAssignedResources(Long authorityId, Set<Long> resourceIds) {
		Authority authority = authorityDAO.getById(authorityId);
		Set<Resource> resourcesSet = new HashSet<Resource>();
		if (!resourceIds.isEmpty()) {
			Condition condition = new Condition("id", resourceIds);
			condition.setOperator(Condition.IN);
			condition.setValueType(Long.class);
			List<Resource> resources = resourceDAO.getByCondition(condition);
			resourcesSet.addAll(resources);
		}
		authority.setResources(resourcesSet);
		authorityDAO.update(authority);
	}

	public Set<Resource> getResourcesByAuthorityId(Long id) {
		Authority authority = authorityDAO.getById(id);
		if (authority != null && authority.getResources().size() > 0) {
			return authority.getResources();
		}
		return new HashSet<Resource>();
	}

	public List<Authority> getAvailableAuthorities(List<Long> notInIds) {
		if (notInIds == null || notInIds.isEmpty()) {
			return authorityDAO.getAll();
		} else {
			Condition condition = new Condition("id", notInIds);
			condition.setOperator(Condition.NOT_IN);
			condition.setValueType(Long.class);
			return authorityDAO.getByCondition(condition);
		}
	}

}
