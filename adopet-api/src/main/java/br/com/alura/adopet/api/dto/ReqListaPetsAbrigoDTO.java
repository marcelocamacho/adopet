package br.com.alura.adopet.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ReqListaPetsAbrigoDTO(@NotBlank String idOuNome) {
    
}
