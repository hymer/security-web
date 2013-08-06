package com.hymer.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hymer.core.CommonService;
import com.hymer.core.dao.AuthorityDAO;
import com.hymer.core.dao.RoleDAO;
import com.hymer.core.entity.Authority;
import com.hymer.core.entity.Role;
import com.hymer.core.model.Condition;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;

@Service
public class RoleService extends CommonService {

	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private AuthorityDAO authorityDAO;
	
	public ResponseJSON query(QueryObject queryObject) {
		Condition condition = new Condition("code", Role.SUPER_ROLE_FLAG);
		condition.setOperator(Condition.NOT_EQ);
		queryObject.getConditions().add(0, condition);
		return roleDAO.getAll(queryObject);
	}

	public Role getRoleById(Long id) {
		return roleDAO.getById(id);
	}

	public Role getRoleByCode(String roleCode) {
		return roleDAO.getByProperty("code", roleCode).get(0);
	}

	public Set<Authority> getAuthoritiesByRoleId(Long id) {
		Role role = roleDAO.getById(id);
		if (role != null && role.getAuthorities().size() > 0) {
			return role.getAuthorities();
		}
		return new HashSet<Authority>();
	}

	public void saveAssignedAuthorities(Long roleId, Set<Long> authorityIds) {
		Role role = roleDAO.getById(roleId);
		if (role.getCode().equals(Role.SUPER_ROLE_FLAG)) {
			throw new RuntimeException("不需要给超级管理员分配权限!");
		} else {
			Set<Authority> authoritySet = new HashSet<Authority>();
			if (!authorityIds.isEmpty()) {
				Condition condition = new Condition("id", authorityIds);
				condition.setOperator(Condition.IN);
				condition.setValueType(Long.class);
				List<Authority> resources = authorityDAO.getByCondition(condition);
				authoritySet.addAll(resources);
			}
			role.setAuthorities(authoritySet);
			roleDAO.update(role);
		}
	}
	
	public List<Role> getAvailables() {
		Condition condition = new Condition("code", Role.SUPER_ROLE_FLAG);
		condition.setOperator(Condition.NOT_EQ);
		return roleDAO.getByCondition(condition);
	}

}
