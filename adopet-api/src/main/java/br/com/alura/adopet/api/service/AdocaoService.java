package br.com.alura.adopet.api.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.adopet.api.dto.ReqAprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReqReprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReqSolicitacaoAdotaoDTO;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.IValidacaoSolicitacaoAdocao;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository repository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private List<IValidacaoSolicitacaoAdocao> validacoes;
;

    public void solicitar(ReqSolicitacaoAdotaoDTO dto) { 

        Pet pet = petRepository.getReferenceById(dto.idPet());

        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        validacoes.forEach(v -> v.validar(dto));
        
        Adocao adocao = new Adocao(tutor,pet,dto.motivo());
        repository.save(adocao);

        emailService.enviarEmail(
            adocao.getPet().getAbrigo().getEmail(),
            "Solicitação de adoção",
            "Olá " + adocao.getPet().getAbrigo().getNome()
                + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome()
                + ". \nFavor avaliar para aprovação ou reprovação.");

    }
    //TODO: disparar exceções
    public void aprovar(ReqAprovacaoAdocaoDTO dto) {
        Adocao adocao = repository.getReferenceById(dto.idAdocao());

        adocao.marcarComoAprovado();

        repository.save(adocao);

        emailService.enviarEmail(
            adocao.getTutor().getEmail(),
            "Adoção aprovada",
            "Parabéns " +adocao.getTutor().getNome() 
            +"!\n\nSua adoção do pet " +adocao.getPet().getNome() 
            +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) 
            +", foi aprovada.\nFavor entrar em contato com o abrigo " 
            +adocao.getPet().getAbrigo().getNome() +" para agendar a busca do seu pet.");

    }

    //TODO: disparar exceções
    public void reprovar(ReqReprovacaoAdocaoDTO dto) {
        Adocao adocao = repository.getReferenceById(dto.idAdocao());
        adocao.marcarComoReprovado(dto.justificativa());
        repository.save(adocao);

        emailService.enviarEmail(
            adocao.getTutor().getEmail(),
            "Adoção reprovada",
            "Olá " +adocao.getTutor().getNome() 
            +"!\n\nInfelizmente sua adoção do pet " +adocao.getPet().getNome() 
            +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) 
            +", foi reprovada pelo abrigo " +adocao.getPet().getAbrigo().getNome() 
            +" com a seguinte justificativa: " +adocao.getJustificativaStatus());
    }
}
