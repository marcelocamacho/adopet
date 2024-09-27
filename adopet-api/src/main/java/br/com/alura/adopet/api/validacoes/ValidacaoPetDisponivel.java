package br.com.alura.adopet.api.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.ReqSolicitacaoAdotaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;

@Component
public class ValidacaoPetDisponivel implements IValidacaoSolicitacaoAdocao {

    @Autowired
    private PetRepository petRepository;

    public void validar(ReqSolicitacaoAdotaoDTO dto){

        Pet pet = petRepository.getReferenceById(dto.idPet());

        if(pet.getAdotado() == true){
            throw new ValidacaoException("Pet já foi adotado!");
        }
    }
}
