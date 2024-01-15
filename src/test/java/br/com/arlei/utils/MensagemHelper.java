package br.com.arlei.utils;

import br.com.arlei.model.Mensagem;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class MensagemHelper {

    public static Mensagem gerarMensagem(){
        // ja gera como registro do banco de dados...
        return Mensagem.builder()
                .id(UUID.randomUUID())
                .usuario("Jose")
                .conteudo("conteudo da mensagem")
                .build();

    }

    public static Mensagem gerarMensagemCompleta() {
        var timestamp = LocalDateTime.now();
        return Mensagem.builder()
                .id(UUID.randomUUID())
                .usuario("joe")
                .conteudo("xpto test")
                .dataCriacao(timestamp)
                .dataAlteracao(timestamp)
                .build();
    }
}
