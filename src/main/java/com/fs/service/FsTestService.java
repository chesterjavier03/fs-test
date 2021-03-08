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

	/**
	 * Strictly followed
	 * (https://developers.facebook.com/docs/facebooklogin/manually-build-a-login-flow)
	 * <p>
	 * Alternative on doing this requirement is to create a separate client application to handle the login flow of facebook (React app)
	 *
	 */
	public String facebookLogin() {
		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.getForObject(String.format("https://www.facebook.com/v10.0/dialog/oauth?client_id=%s&display=popup&response_type=token&redirect_uri=%s&state=%s",
					"1336848609802188", "https://www.facebook.com/connect/login_success.html", "st=state123abc,ds=123456789"), String.class);
		} catch (HttpClientErrorException e) {
			log.error("ERROR", e.getRawStatusCode() + " - " + e.getStatusCode().getReasonPhrase());
			log.error("ERROR", e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * NO SIGNUP FUNCTION ON THE URL PROVIDED
	 * (https://developers.facebook.com/docs/facebooklogin/manually-build-a-login-flow)
	 * <p>
	 * Alternative on doing this requirement is to create a separate client application to handle the login flow of facebook (React app)
	 */
	public String facebookSignup() {
		return null;
	}
}
