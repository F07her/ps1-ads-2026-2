package br.edu.fatecfranca.api.dtos;


import java.time.LocalDate;

public record CarRequest(
    String model,
    String brand,
    String color,
    Integer manufactureYear,
    String licensePlate,
    LocalDate registrationDate,
    Boolean imported,
    Long customerId
) {}
