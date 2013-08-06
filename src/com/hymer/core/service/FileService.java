package com.hymer.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hymer.core.CommonService;
import com.hymer.core.dao.FileDAO;
import com.hymer.core.dto.FileDTO;
import com.hymer.core.entity.FileEntity;
import com.hymer.core.entity.User;
import com.hymer.core.model.Condition;
import com.hymer.core.model.QueryObject;
import com.hymer.core.model.ResponseJSON;
import com.hymer.core.util.FileUtils;

@Service
public class FileService extends CommonService {
	@Autowired
	private FileDAO fileDAO;

	public FileEntity getById(Long id) {
		return fileDAO.getById(id);
	}

	@SuppressWarnings("unchecked")
	public ResponseJSON query(QueryObject queryObject) {
		List<Condition> realConditions = new ArrayList<Condition>();
		for (Condition condition : queryObject.getConditions()) {
			if (condition.getValue() == null
					|| !StringUtils.hasText(condition.getValue().toString())) {
				continue;
			}
			if (condition.getKey().equals("fileName")
					|| condition.getKey().equals("attachmentType")) {
				condition.setOperator(Condition.LIKE);
				realConditions.add(condition);
			}
		}
		queryObject.setConditions(realConditions);
		ResponseJSON json = fileDAO.getAll(queryObject);
		List<FileDTO> dtos = new ArrayList<FileDTO>();
		for (FileEntity entity : (List<FileEntity>) json.getData().get("data")) {
			FileDTO dto = new FileDTO(entity);
			dtos.add(dto);
		}
		json.getData().put("data", dtos);
		return json;
	}

	public void saveAll(Map<String, MultipartFile> files, User owner) {
		for (MultipartFile file : files.values()) {
			FileEntity entity = FileUtils.writeFile(file);
			entity.setOwner(owner);
			fileDAO.save(entity);
		}
	}

}
