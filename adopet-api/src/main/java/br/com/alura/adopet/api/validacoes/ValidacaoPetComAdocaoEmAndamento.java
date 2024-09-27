package br.com.alura.adopet.api.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.ReqSolicitacaoAdotaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;

@Component
public class ValidacaoPetComAdocaoEmAndamento implements IValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void validar(ReqSolicitacaoAdotaoDTO dto){

        boolean petTemAdocaoEmAndamento = adocaoRepository
            .existsByPetIdAndStatus(
                dto.idPet(),
                StatusAdocao.AGUARDANDO_AVALIACAO
        );
        
        if(petTemAdocaoEmAndamento){
                throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado");
        }
        
    }
}
