package br.com.alura.adopet.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.alura.adopet.api.dto.AprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.SolicitacaoAdotaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;

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
;

    public void solicitar(SolicitacaoAdotaoDTO dto) { 

        Pet pet = petRepository.getReferenceById(dto.idPet());

        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        if (pet.getAdotado() == true) {
            throw new ValidacaoException("Pet já foi adotado!");
        } else {
            List<Adocao> adocoes = repository.findAll();
            for (Adocao a : adocoes) {
                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
                }
            }
            for (Adocao a : adocoes) {
                if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
                }
            }
            for (Adocao a : adocoes) {
                int contador = 0;
                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                    contador = contador + 1;
                }
                if (contador == 5) {
                    throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
                }
            }
        }
        if(tutor == null){
            throw new ValidacaoException("Tutor inválido!");
        }
        
        Adocao adocao = new Adocao();
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocao.setPet(pet);
        adocao.setTutor(tutor);
        adocao.setMotivo(dto.motivo());

        repository.save(adocao);


        emailService.enviarEmail(
            adocao.getPet().getAbrigo().getEmail(),
            "Solicitação de adoção",
            "Olá " + adocao.getPet().getAbrigo().getNome()
                + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " + adocao.getPet().getNome()
                + ". \nFavor avaliar para aprovação ou reprovação.");

    }
    //TODO: disparar exceções
    public void aprovar(AprovacaoAdocaoDTO dto) {
        Adocao adocao = repository.getReferenceById(dto.idAdocao());

        adocao.setStatus(StatusAdocao.APROVADO);

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
    public void reprovar(ReprovacaoAdocaoDTO dto) {
        Adocao adocao = repository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.REPROVADO);
        adocao.setJustificativaStatus(dto.justificativa());
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
