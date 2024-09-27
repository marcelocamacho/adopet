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
public class ValidacaoTutorComLimiteAdocoes {
    
    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private AdocaoRepository adocaoRepository;

    private long MAX_ADOCOES = 5;

    public void validar(ReqSolicitacaoAdotaoDTO dto){
        List<Adocao> adocoes = adocaoRepository.findAll();
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
        int contadorAdocoes = 0;
        for(Adocao a: adocoes){
            if(a.getTutor() == tutor && a.getStatus()==StatusAdocao.APROVADO){
                contadorAdocoes+=1;
            }
        }
        if(contadorAdocoes>=MAX_ADOCOES){
            throw new ValidacaoException("Tutor já possui o limite de pets adotados!");
        }
    }
}
