package br.com.alura.adopet.api.dto;
import java.util.List;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;

public record RespListaAbrigosDTO(
    Long id,
    String nome,
    String telefone,
    String email,
    List<Pet> pets
) {
    public RespListaAbrigosDTO(Abrigo abrigo){
        this(
            abrigo.getId(), 
            abrigo.getNome(), 
            abrigo.getTelefone(), 
            abrigo.getEmail(), 
            abrigo.getPets());
    }
}