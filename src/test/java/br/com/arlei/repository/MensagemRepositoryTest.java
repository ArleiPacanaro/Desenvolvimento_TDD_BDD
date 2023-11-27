package br.com.arlei.repository;


import br.com.arlei.model.Mensagem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.*;

public class MensagemRepositoryTest  {


    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Test
    void devePermitirRegistrarMensagem(){
        //Triple A

        // Arrange prepara

        var mensagem = gerarMensagem();

        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        //Act -- executa
        var mensagemArmazenada = mensagemRepository.save(mensagem);


        // Assert -- verifica
        assertThat(mensagemArmazenada).isNotNull().isEqualTo(mensagem);

        // garanto que tive uma chamada a banco de dados... q
        verify(mensagemRepository, times(1)).save(mensagem);


        //fail("teste não implementado");

    }

    @Test
    void devePermitirRemoverMensagem(){

        // Arrange
        var id = UUID.randomUUID();
        doNothing().when(mensagemRepository).deleteById(any(UUID.class));
        // Act
        mensagemRepository.deleteById(id);
        //Assert
        verify(mensagemRepository,times(1)).deleteById(any(UUID.class));

        //fail("teste não implementado");

    }

    @Test
    void devePermitirBuscarMensagem(){

        //arrange
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mensagem));

        // act
        var mensagemRecebidaOpcional = mensagemRepository.findById(id);


        // Assert

        assertThat(mensagemRecebidaOpcional)
                .isPresent()
                .containsSame(mensagem);

        mensagemRecebidaOpcional.ifPresent( mensagemRecebida -> {
                    assertThat(mensagemRecebida.getId()).isEqualTo(mensagem.getId());
                    assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());

                }
        );

        verify(mensagemRepository,times(1)).findById(any(UUID.class));
        //fail("teste não implementado");

    }

    @Test
    void devePermitirListarMensagens(){

        // Arrange
        var mensagem1 = gerarMensagem();
        var mensagem2 = gerarMensagem();

        var listaMensagens = Arrays.asList(
          mensagem1,
          mensagem2
        );

        when(mensagemRepository.findAll()).thenReturn(listaMensagens);

        // Act
        var mensagensRecebidas = mensagemRepository.findAll();

        // Assert
           assertThat(mensagensRecebidas)
                   .hasSize(2)
                   .containsExactlyInAnyOrder(mensagem1,mensagem2);

    }

    private Mensagem gerarMensagem(){
        // ja gera como registro do banco de dados...
        return Mensagem.builder()
                .id(UUID.randomUUID())
                .usuario("Jose")
                .conteudo("conteudo da mensagem")
                .build();

    }

}