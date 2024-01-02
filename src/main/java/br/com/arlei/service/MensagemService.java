package br.com.arlei.service;

import br.com.arlei.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MensagemService {

    Mensagem registrarMensagem(Mensagem mensagem);
    Mensagem buscarMensagem(UUID id);
    Mensagem alterarMensagem(UUID id, Mensagem mensagemNova);
    Boolean removerMensagem(UUID id);
    Page<Mensagem> listarMensagens(Pageable page);



}
