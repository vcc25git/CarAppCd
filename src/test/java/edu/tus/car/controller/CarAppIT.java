/*
 * Integration Testing with TestRestTemplate
 * 
 */

package edu.tus.car.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tus.car.CarAppApplication;
import edu.tus.car.model.Car;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarAppApplication.class, 
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarAppIT {
	
	
	@Value(value="${local.server.port}")
	private int port;
	
	TestRestTemplate restTemplate;
	HttpHeaders headers;
	
	@BeforeEach
	public void setup() {
		restTemplate =new TestRestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	
	@Test
	@Sql({"/testdata.sql"})
	public void addCarSuccessIntTest()
	{
		
		HttpEntity<Car> request = new HttpEntity<Car>(buildCar(),headers);
		ResponseEntity<String>	response =	restTemplate.postForEntity(
				"http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	@Test
	@Sql({"/testdata.sql"})
	public void addCarEmptyFieldIntTest()
	{
		Car car=buildCar();
		car.setMake("");
		HttpEntity<Car> request = new HttpEntity<Car>(car,headers);
		ResponseEntity<String>	response =	restTemplate.postForEntity(
				"http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	
	@Test
	@Sql({"/testdata.sql"})
	public void addCarYearInvalidIntTest()
	{
		Car car=buildCar();
		car.setYear(19);
		HttpEntity<Car> request = new HttpEntity<Car>(car,headers);
		ResponseEntity response =	restTemplate.postForEntity(
				"http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	@Sql({"/testdata.sql"})
	public void addCarColourInvalidIntTest()
	{
		Car car=buildCar();
		car.setColor("Brown");
		HttpEntity<Car> request = new HttpEntity<Car>(car,headers);
		ResponseEntity response =	restTemplate.postForEntity(
				"http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	
	@Test
	@Sql({"/testdata.sql"})
	public void addCarMakeModelInvalidIntTest()
	{
		Car car=buildCar();
		car.setMake("Audi");
		car.setModel("A1");
		HttpEntity<Car> request = new HttpEntity<Car>(car,headers);
		ResponseEntity response =	restTemplate.postForEntity(
				"http://localhost:"+port+"/api/cars", request, String.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	
	
		
	Car buildCar() {
		Car car = new Car();
		car.setMake("Prosche");;
		car.setModel("GT4");
		car.setColor("Green");
		car.setYear(2023);
		return car;
	}

}
