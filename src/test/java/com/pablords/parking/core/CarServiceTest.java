package com.pablords.parking.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.services.CarService;
import com.pablords.parking.core.valueObjects.Plate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

class CarServiceTest {
    private CarRepositoryPort carRepositoryPortMock;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepositoryPortMock = mock(CarRepositoryPort.class); // Criando o mock do repositório
        carService = new CarService(carRepositoryPortMock); // Injetando o mock
    }

    @Test
    void testCreateCarWithDuplicatePlateThrowsException() {

        var plate1 = new Plate("ABC1234");
        var plate2 = new Plate("ABC1234");
        Car newCar = new Car(plate2, "Honda", "Green", "Civic");

        when(carRepositoryPortMock.existsByPlate(plate1.getValue())).thenReturn(true);

        // Act & Assert
        ExistPlateException exception = assertThrows(
                ExistPlateException.class,
                () -> carService.create(newCar));

        assertEquals(String.format(ErrorMessages.CAR_WITH_PLATE_EXISTS, newCar.getPlate().getValue()), exception.getMessage());
        verify(carRepositoryPortMock, never()).save(any(Car.class)); // O método create não deve ser chamado
    }

    @Test
    void testCreateCarWithUniquePlate() {
        var plate = new Plate("XYZ5678");
        Car newCar = new Car(plate, "Honda", "Civic", "Green");

        when(carRepositoryPortMock.find()).thenReturn(List.of());
        when(carRepositoryPortMock.save(newCar)).thenReturn(newCar);

        // Act
        Car createdCar = carService.create(newCar);

        // Assert
        assertNotNull(createdCar);
        assertEquals("XYZ5678", createdCar.getPlate().getValue());
        verify(carRepositoryPortMock, times(1)).save(newCar);
    }

    @Test
    void testFind() {
        // Arrange
        List<Car> carList = new ArrayList<>();

        var hondaCar = new Car();
        var plateHondaCar = new Plate("XYZ5678");
        hondaCar.setBrand("Honda");
        hondaCar.setColor("Blue");
        hondaCar.setModel("Civic");
        hondaCar.setPlate(plateHondaCar);

        var fordCar = new Car();
        var plateFordCar = new Plate("LMN3456");
        fordCar.setBrand("Ford");
        fordCar.setColor("Blue");
        fordCar.setModel("Focus");
        fordCar.setPlate(plateFordCar);

        carList.add(hondaCar);
        carList.add(fordCar);

        when(carRepositoryPortMock.find()).thenReturn(carList);

        // Act
        List<Car> result = carService.find();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepositoryPortMock, times(1)).find();
    }
}
