package br.edu.fatecfranca.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message = "O nome completo é obrigatório.")
    String fullname,

    @NotBlank(message = "O nome de usuário é obrigatório.")
    @Pattern(
        regexp = "^[a-zA-Z]+$",
        message = "O nome de usuário deve conter apenas letras sem acentos, números, ponto, hífen e sublinhado."
    )
    String username,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ter um formato válido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    String password,

    @NotNull(message = "O tipo de usuário é obrigatório.")
    Boolean isAdmin
) {}
