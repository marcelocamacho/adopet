package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.ReqSolicitacaoAdotaoDTO;

public interface IValidacaoSolicitacaoAdocao {
    void validar(ReqSolicitacaoAdotaoDTO dto);
}
