package edu.tus.car.errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.tus.car.errors.ErrorValidation;
import edu.tus.car.model.Car;

public class ErrorValidationTest {
	
	ErrorValidation errorValidation;
	Car car;

	@BeforeEach
	void setUp() {
		errorValidation = new ErrorValidation();
		car = buildCar();
	}

	@Test
	void testMakeAndModelNotAllowed() {
		assertFalse(errorValidation.checkMakeAndModelNotAllowed(car));
	}

	//colour
	@Test
	void testInvalidColour() {
		car.setColor("Orange");
		assertTrue(errorValidation.colorNotOK(car));
	}

	//year
	@Test
	void testInvalidYear() {
		car.setYear(19);
		assertTrue(errorValidation.yearNotOK(car));
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
