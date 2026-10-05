package br.edu.fatecfranca.api.dtos;

public record UserRequest(
    String fullname,
    String username,
    String email,
    String password,
    Boolean isAdmin
) {}
