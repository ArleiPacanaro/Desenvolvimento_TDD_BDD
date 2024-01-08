package br.com.arlei.repository;


import br.com.arlei.model.Mensagem;
import br.com.arlei.utils.MensagemHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.*;

class MensagemRepositoryTest  {


    // Mockito que cria os Fakes
    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable openMocks;

    /// Antes de cada execução, cria um novo mock , fake, ou seja inicia todas as variaves que tem a anotação Mock
    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    // Após cada execução...
    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    // JUNIT
    @Test
    void devePermitirRegistrarMensagem(){
        //Triple A

        // Arrange prepara

        var mensagem = MensagemHelper.gerarMensagem();

        // do mockito, quando passamos a classe deixamos bem generica que e a classe
        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        //Act -- executa
        var mensagemArmazenada = mensagemRepository.save(mensagem);


        // Assert -- verifica
        assertThat(mensagemArmazenada).isNotNull().isEqualTo(mensagem);

        // garanto que tive uma chamada a banco de dados...  do mockito... no sabe para deixa generico poderia usar Mensagem.class com Any()
        // e o que chamamos de spy
        verify(mensagemRepository, times(1)).save(mensagem);


        //fail("teste não implementado");

    }

    @Test
    void devePermitirRemoverMensagem(){

        // Arrange
        var id = UUID.randomUUID();
        // metodo do Mockito....
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
        var mensagem = MensagemHelper.gerarMensagem();
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
        var mensagem1 = MensagemHelper.gerarMensagem();
        var mensagem2 = MensagemHelper.gerarMensagem();

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

}
