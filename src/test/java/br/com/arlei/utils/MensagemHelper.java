package br.com.arlei.utils;

import br.com.arlei.model.Mensagem;

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
        return Mensagem.builder()
                .usuario("joe")
                .conteudo("xpto test")
                .build();
    }
}
