package br.com.alura.adopet.api.validacoes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alura.adopet.api.dto.ReqSolicitacaoAdotaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;

@Component
public class ValidacaoTutorComAdocoesEmAndamento {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private TutorRepository tutorRepository;

    public void validar(ReqSolicitacaoAdotaoDTO dto){

        List<Adocao> adocoes = adocaoRepository.findAll();
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
        
        for (Adocao a : adocoes){
            if(a.getTutor() == tutor && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO){
                throw new ValidacaoException("Tutor já possui outro pedido de adoção em análise");
            }
        }
    }
}
