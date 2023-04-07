/*
 * Unit Testing of CarService using Mockito
 * These tests use Mockito to mock the dependencies of the CarService class 
 * (CarRepository and ErrorValidation) and test each method of the class. 
 * The tests verify that the correct methods of the dependencies are called and that the correct exceptions are thrown when necessary. 
 *
 */

package edu.tus.car.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.tus.car.dao.CarRepository;
import edu.tus.car.errors.ErrorValidation;
import edu.tus.car.exception.CarNotFoundException;
import edu.tus.car.exception.CarValidationException;
import edu.tus.car.model.Car;
import edu.tus.car.service.CarService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepo;

    @Mock
    private ErrorValidation errorValidation;

    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        carService = new CarService();
        carService.carRepo = carRepo;
        carService.errorValidation = errorValidation;
        car = buildCar();
    }

    @Test
    void testGetAllCars() {
        List<Car> expectedCars = Arrays.asList(car);
        Mockito.when(carRepo.findAll()).thenReturn(expectedCars);
        List<Car> actualCars = carService.getAllCars();
        Assertions.assertEquals(expectedCars, actualCars);
    }

    @Test
    void testGetCarById() {
        Mockito.when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        Optional<Car> actualCar = carService.getCarById(1L);
        Assertions.assertEquals(Optional.of(car), actualCar);
    }

    @Test
    void testCreateCar() throws CarValidationException {
        Mockito.when(errorValidation.checkMakeAndModelNotAllowed(car)).thenReturn(false);
        Mockito.when(errorValidation.yearNotOK(car)).thenReturn(false);
        Mockito.when(errorValidation.colorNotOK(car)).thenReturn(false);
        Mockito.when(carRepo.save(car)).thenReturn(car);
        Car actualCar = carService.createCar(car);
        Assertions.assertEquals(car, actualCar);
    }

    @Test
    void testCreateCarThrowsException() {
        Mockito.when(errorValidation.checkMakeAndModelNotAllowed(car)).thenReturn(true);
        Assertions.assertThrows(CarValidationException.class, () -> {
            carService.createCar(car);
        });
    }

    @Test
    void testDeleteCar() throws CarNotFoundException {
        Mockito.when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        carService.deleteCar(1L);
        Mockito.verify(carRepo).delete(car);
    }

    @Test
    void testDeleteCarThrowsException() {
        Mockito.when(carRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(CarNotFoundException.class, () -> {
            carService.deleteCar(1L);
        });
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
