package br.com.alura.adopet.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ReqCadastraAbrigoDTO(@NotBlank String nome, @NotBlank String telefone, @NotBlank String email) {
}
