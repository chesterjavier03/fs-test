package com.fs.controller;

import com.fs.dto.FsTestDto;
import com.fs.dto.FsTestFbDto;
import com.fs.service.FsTestService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by chesterjavier.
 */
@RestController
@Slf4j
public class FsTestController {

	@Autowired
	private FsTestService fsTestService;

	@PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addId(@RequestBody String name) {
		try {
			log.info("[FsTestController] - Adding new id into the database with name [{}]", name);
			fsTestService.addId(name);
			JSONObject json = new JSONObject();
			json.put("json", "Successfully added a new ID");
			return new ResponseEntity(json, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("[FsTestController] ERROR [{}]", e.getLocalizedMessage());
			return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteId(@PathVariable("id") final Integer id) {
		log.info("[FsTestController] - Deleting data with ID [{}]", id);
		fsTestService.deleteId(id);
		return new ResponseEntity<String>(String.format("Successfully deleted Identification data with ID [%s]", id), HttpStatus.OK);
	}

	@PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateId(@RequestBody FsTestDto fsTestDto) {
		try {
			log.info("[FsTestController] - Updating data with ID [{}]", fsTestDto.getId());
			fsTestService.updateId(fsTestDto);
			JSONObject json = new JSONObject();
			json.put("json", "Successfully updated ID");
			return new ResponseEntity(json, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			log.error("[FsTestController] ERROR [{}]", e.getLocalizedMessage());
			return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity listId() {
		log.info("[FsTestController] - Fetching all IDs......");
		return Optional.ofNullable(fsTestService.fetchAllId()).map(result -> new ResponseEntity(result, HttpStatus.OK))
				.orElse(new ResponseEntity("No data found", HttpStatus.NOT_FOUND));
	}

	/**
	 * Strictly followed (https://developers.facebook.com/docs/facebooklogin/manually-build-a-login-flow) from the technical exam documentation
	 */
	@PostMapping(path = "/fb-login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String fbLogin(@RequestBody FsTestFbDto fsTestFbDto) {
		log.info("[FsTestController] - Execute Facebook login......");
		return fsTestService.facebookLogin();
	}

	/**
	 * Strictly followed (https://developers.facebook.com/docs/facebooklogin/manually-build-a-login-flow) from the technical exam documentation
	 */
	@PostMapping(path = "/fb-signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String fbSignUp(@RequestBody FsTestFbDto fsTestFbDto) {
		log.info("[FsTestController] - Execute Facebook signup......");
		return fsTestService.facebookSignup();
	}
}
