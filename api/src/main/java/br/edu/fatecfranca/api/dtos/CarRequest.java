package br.edu.fatecfranca.api.dtos;


import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;

public record CarRequest(

    @NotBlank(message = "O modelo do carro é obrigatório.")
    String model,

    @NotBlank(message = "A marca do carro é obrigatória.")
    String brand,

    @NotBlank(message = "A cor do carro é obrigatória.")
    String color,

    @NotNull(message = "O ano de fabricação do carro é obrigatório.")
    @Positive(message = "O ano de fabricação do carro deve ser um valor maior que zero.")
    Integer manufactureYear,

    @NotBlank(message = "A placa do carro é obrigatória.")
    @Pattern(
        regexp = "^[A-Z]{3}-\\d{4}$",
        message = "A placa do carro deve ter o formato ABC-1234."
    )
    String licensePlate,

    @NotNull(message = "A data de registro do carro é obrigatória.")
    @PastOrPresent(message = "A data de registro do carro deve ser uma data passada ou presente.")
    LocalDate registrationDate,

    @NotNull(message = "A informação de importação do carro é obrigatória.")
    Boolean imported,

    @Positive(message = "O ID do cliente deve ser um valor maior que zero.")
    Long customerId

) {}
