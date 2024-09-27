package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.ReqAprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReqReprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReqSolicitacaoAdotaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(@RequestBody @Valid ReqSolicitacaoAdotaoDTO dto) {
        try{
            this.adocaoService.solicitar(dto);
            return ResponseEntity.ok("Adoção solicitada com sucesso!");
        } catch(ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid ReqAprovacaoAdocaoDTO dto) {

        try{
            this.adocaoService.aprovar(dto);
            return ResponseEntity.ok().build();
        } catch(ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid ReqReprovacaoAdocaoDTO dto) {
        try{
            this.adocaoService.reprovar(dto);
            return ResponseEntity.ok().build();
        } catch(ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

}
