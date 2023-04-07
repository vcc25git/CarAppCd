package edu.tus.car.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.tus.car.controller.CarController;
import edu.tus.car.errors.ErrorMessage;
import edu.tus.car.errors.ErrorMessages;
import edu.tus.car.exception.CarValidationException;
import edu.tus.car.model.Car;
import edu.tus.car.service.CarService;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerMockMvcTest {
	
	@Autowired
	CarController carController;
	
	@MockBean
	CarService carService;
	

	@Test
	public void addCarTestSuccess() throws CarValidationException
	{
		Car car = buildCar();
		Car savedCar = buildCar();
		savedCar.setId(1L);
		when(carService.createCar(car)).thenReturn(savedCar);
		ResponseEntity response	= carController.addCar(car);
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		Car employeeAdded= (Car) response.getBody();
		employeeAdded.getId();
		assertEquals(1L,employeeAdded.getId());
		assertTrue(employeeAdded.equals(savedCar));
	}
	
	@Test
	public void addCarTestFail() throws CarValidationException
	{
		Car car = buildCar();
		Car savedCar = buildCar();
		savedCar.setId(1L);
		when(carService.createCar(car)).thenThrow(new CarValidationException(ErrorMessages.INVALID_MAKE_MODEL.getMsg()));		
		ResponseEntity response	=carController.addCar(car);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		ErrorMessage errorMsg= (ErrorMessage) response.getBody();
		assertEquals(ErrorMessages.INVALID_MAKE_MODEL.getMsg(),errorMsg.getErrorMessage());
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
