package com.fs.service;

import com.fs.dto.FsTestDto;
import com.fs.entity.FsTestEntity;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * Created by chesterjavier.
 */
@Service
@Slf4j
@Repository
@Transactional
public class FsTestService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	public void addId(String name) {
		log.info("[FsTestService] - Adding new data into the database....");
		em.persist(new FsTestEntity(name));
		em.flush();
	}

	public List<FsTestEntity> fetchAllId() {
		log.info("[FsTestService] - Fetching all id in the database....");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<FsTestEntity> query = builder.createQuery(FsTestEntity.class);
		Root<FsTestEntity> fsTestEntityRoot = query.from(FsTestEntity.class);
		query.select(fsTestEntityRoot);
		TypedQuery<FsTestEntity> result = em.createQuery(query);
		List<FsTestEntity> resultList = result.getResultList();
		return result.getResultList().size() > 0 ? resultList : Collections.emptyList();
	}

	public void updateId(FsTestDto dto) {
		log.info("[FsTestService] - Update data with name [{}]", dto.getId());
		em.merge(new FsTestEntity(dto));
		em.flush();
	}

	public void deleteId(Integer id) {
		log.info("[FsTestService] - Deleting data with ID [{}]", id);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<FsTestEntity> delete = builder.createCriteriaDelete(FsTestEntity.class);
		Root<FsTestEntity> fsTestEntityRoot = delete.from(FsTestEntity.class);
		Predicate fsTestId = builder.equal(fsTestEntityRoot.get("id"), id);
		delete.where(fsTestId);
		em.createQuery(delete).executeUpdate();
	}
}
