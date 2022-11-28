package com.cargame.cargame.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cargame.cargame.dto.GameInfo;
import com.cargame.cargame.dto.Position;

class CarGameControllerTest {

	private String url;
	private HttpHeaders headers;
	private final RestTemplate restTemplate = new RestTemplateBuilder().build();
	private GameInfo gameInfo;

	@BeforeEach
	void setUp() {
		url = "http://localhost:8080/startNewGame";

		// create headers
		headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// send POST request
		ResponseEntity<GameInfo> response = null;
		try {
			response = this.restTemplate.getForEntity(url, GameInfo.class);
			gameInfo = response.getBody();
		} catch (Exception e) {
			System.out.println(e);
			assertEquals(HttpClientErrorException.BadRequest.class, e.getClass());
		}
	}

	@Test
	void playTurn_MoveDown_Pass() {
		url = "http://localhost:8080/playTurn";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dir", 0);

		// create headers
		headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// send POST request
		ResponseEntity<GameInfo> response = null;

		// build the request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

		Position carPos = this.gameInfo.map.getCarPosition();
		response = this.restTemplate.postForEntity(url, entity, GameInfo.class);
		this.gameInfo = response.getBody();
		
		Position newCarPos = new Position();
		newCarPos.setX(this.gameInfo.map.getCarPosition().getX());
		newCarPos.setY(this.gameInfo.map.getCarPosition().getY());
		assertNotEquals(carPos, newCarPos);

	}

	@Test
	void playFullGame_RandomDirections1000Lives_Pass() {

		url = "http://localhost:8080/setLives";
		
		Map<String, Object> mapLives = new HashMap<String, Object>();
		mapLives.put("dir", 1000);
		
		// create headers
		headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		// send POST request
		ResponseEntity<GameInfo> responseLives = null;
		
		// build the request
		HttpEntity<Map<String, Object>> entityLives = new HttpEntity<>(mapLives, headers);
		
		responseLives = this.restTemplate.postForEntity(url, entityLives, GameInfo.class);
		
		while(this.gameInfo.gameState == 0) {
			
			int direction = new Random().nextInt(4);
			url = "http://localhost:8080/playTurn";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dir", direction);
			
			// create headers
			headers = new HttpHeaders();
			// set `content-type` header
			headers.setContentType(MediaType.APPLICATION_JSON);
			// set `accept` header
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			
			// send POST request
			ResponseEntity<GameInfo> response = null;
			
			// build the request
			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
			
			response = this.restTemplate.postForEntity(url, entity, GameInfo.class);
			this.gameInfo = response.getBody();
		}
	
		assertThat(this.gameInfo.lives > 0).isTrue();
		assertEquals(1, this.gameInfo.gameState);
		assertEquals("VICTORY!\n", this.gameInfo.turnMessage);
		
	}
	
	@Test
	void playFullGame_RandomDirections0Lives_Fail() {

		url = "http://localhost:8080/setLives";
		
		Map<String, Object> mapLives = new HashMap<String, Object>();
		mapLives.put("dir", 0);
		
		// create headers
		headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		// send POST request
		ResponseEntity<GameInfo> responseLives = null;
		
		// build the request
		HttpEntity<Map<String, Object>> entityLives = new HttpEntity<>(mapLives, headers);
		
		responseLives = this.restTemplate.postForEntity(url, entityLives, GameInfo.class);
		
		while(this.gameInfo.gameState == 0) {
			
			int direction = new Random().nextInt(4);
			url = "http://localhost:8080/playTurn";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dir", direction);
			
			// create headers
			headers = new HttpHeaders();
			// set `content-type` header
			headers.setContentType(MediaType.APPLICATION_JSON);
			// set `accept` header
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			
			// send POST request
			ResponseEntity<GameInfo> response = null;
			
			// build the request
			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
			
			response = this.restTemplate.postForEntity(url, entity, GameInfo.class);
			this.gameInfo = response.getBody();
		}
		
		System.out.println(this.gameInfo.turnMessage);
		
		assertEquals(1, this.gameInfo.gameState);
		assertThat(this.gameInfo.lives == 0).isTrue();
		assertEquals("GAME IS OVER. \nStart a new game.", this.gameInfo.turnMessage);;
		
	}
	
	@Test
	void playTurn_WrongInput_Fail() {

		url = "http://localhost:8080/playTurn";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("map", 5);

		// create headers
		headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// send POST request
		ResponseEntity<GameInfo> response = null;
		// build the request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
		try {
			response = this.restTemplate.postForEntity(url, entity, GameInfo.class);
			gameInfo = response.getBody();
		} catch (Exception e) {
			System.out.println(e);
			assertEquals(HttpClientErrorException.BadRequest.class, e.getClass());
		}
	}

}
