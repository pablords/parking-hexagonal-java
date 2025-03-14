package com.pablords.parking.unit.core.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.exceptions.ErrorMessages;
import com.pablords.parking.core.exceptions.ExistPlateException;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.services.CarService;
import com.pablords.parking.core.valueobjects.Plate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

class CarServiceUnitTest {
  private CarRepositoryPort carRepositoryPortMock;
  private CarService carService;

  @BeforeEach
  void setUp() {
    carRepositoryPortMock = mock(CarRepositoryPort.class); // Criando o mock do repositório
    carService = new CarService(carRepositoryPortMock); // Injetando o mock
  }

  @Nested
  @DisplayName("Testes para criação de carro")
  class CreateCarTests {

    @Test
    @DisplayName("Deve lançar ExistPlateException ao tentar criar um carro com placa duplicada")
    void shouldThrowException_WhenCreatingCarWithDuplicatePlate() {
      var plate1 = new Plate("ABC1234");
      var plate2 = new Plate("ABC1234");
      Car newCar = new Car(plate2, "Honda", "Green", "Civic");

      when(carRepositoryPortMock.existsByPlate(plate1.getValue())).thenReturn(true);

      // Act & Assert
      ExistPlateException exception = assertThrows(
          ExistPlateException.class,
          () -> carService.create(newCar));

      assertEquals(String.format(ErrorMessages.CAR_WITH_PLATE_EXISTS, newCar.getPlate().getValue()),
          exception.getMessage());
      verify(carRepositoryPortMock, never()).save(any(Car.class)); // O método create não deve ser chamado
    }

    @Test
    @DisplayName("Deve criar um carro com placa única")
    void shouldCreateCar_WhenPlateIsUnique() {
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
  }

  @Nested
  @DisplayName("Testes para busca de carros")
  class FindCarTests {

    @Test
    @DisplayName("Deve retornar uma lista de carros")
    void shouldReturnCarList_WhenCarsExist() {
      // Arrange
      List<Car> carList = new ArrayList<>();

      var carHonda = new Car();
      var plateHondaCar = new Plate("XYZ5678");
      carHonda.setBrand("Honda");
      carHonda.setColor("Blue");
      carHonda.setModel("Civic");
      carHonda.setPlate(plateHondaCar);

      var carFord = new Car();
      var plateFordCar = new Plate("LMN3456");
      carFord.setBrand("Ford");
      carFord.setColor("Blue");
      carFord.setModel("Focus");
      carFord.setPlate(plateFordCar);

      carList.add(carHonda);
      carList.add(carFord);

      when(carRepositoryPortMock.find()).thenReturn(carList);

      // Act
      List<Car> result = carService.find();

      // Assert
      assertNotNull(result);
      assertEquals(2, result.size());
      verify(carRepositoryPortMock, times(1)).find();
    }
  }
}
