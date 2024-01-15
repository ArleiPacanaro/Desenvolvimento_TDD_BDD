package br.com.arlei.service;

import br.com.arlei.exception.MensagemNotFoundException;
import br.com.arlei.model.Mensagem;
import br.com.arlei.repository.MensagemRepository;
import br.com.arlei.utils.MensagemHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class MensagemServiceTest {

    // Trabalhar com Fake - Mockito.
    AutoCloseable mock;
    private MensagemService mensagemService;
    @Mock
    private  MensagemRepository mensagemRepository;

    @BeforeEach  // antes de cada teste
    void setup(){

        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImpl(mensagemRepository);
    }

    // Limpar o mock da memoria.
    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirRegistrarMensagem()
    {
        // conceito do triple A
        // Arrange
        var mensagem = MensagemHelper.gerarMensagem();

        // Mockito
        when(mensagemRepository.save(any(Mensagem.class)))
             .thenAnswer( i -> i.getArgument(0));

        // Act
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        // Assert

        assertThat(mensagemRegistrada).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemRegistrada.getUsuario()).isEqualTo(mensagem.getUsuario());
        assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagem.getId()).isNotNull();
        verify(mensagemRepository,times(1)).save(any(Mensagem.class));

    }


    @Test
    void devePermitirBuscarMensagem(){
        // conceito do triple A
        // Arrange
        var id = UUID.randomUUID();
        var mensagem = MensagemHelper.gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mensagem)); /// mockado \ fake o retorno.

        // Act
        var mensagemObtida = mensagemService.buscarMensagem(id);

        //Assert

        assertThat(mensagemObtida).isEqualTo(mensagem);
        verify(mensagemRepository,times(1)).findById(any(UUID.class));

    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste(){

        var id = UUID.randomUUID();
        when(mensagemRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> mensagemService.buscarMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");

        verify(mensagemRepository,times(1)).findById(id);
    }

    @Test
    void devePermitirAlterarMensagem(){

        var id = UUID.randomUUID();
        var mensagemAntiga = MensagemHelper.gerarMensagem();
        mensagemAntiga.setId(id);
        var mensagemNova = mensagemAntiga;
        mensagemNova.setConteudo("abcd");

        when(mensagemRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mensagemAntiga));

        when(mensagemRepository.save(any(Mensagem.class)))
                .thenAnswer(i -> i.getArgument(0));

        var mensagemObtida = mensagemService
                .alterarMensagem(id, mensagemNova);

        assertThat(mensagemObtida)
                .isInstanceOf(Mensagem.class)
                .isNotNull();
        assertThat(mensagemObtida.getId())
                .isEqualTo(mensagemNova.getId());
        assertThat(mensagemObtida.getUsuario())
                .isEqualTo(mensagemNova.getUsuario());
        assertThat(mensagemObtida.getConteudo())
                .isEqualTo(mensagemNova.getConteudo());
        verify(mensagemRepository, times(1)).save(any(Mensagem.class));


    }

    //devePermitirRemoverMensagem

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente(){

        //Arrange
        var id = UUID.randomUUID();
        var mensagemAntiga =MensagemHelper.gerarMensagem();
        mensagemAntiga.setId(id);
        var mensagemNova = MensagemHelper.gerarMensagem();
        mensagemNova.setId(UUID.randomUUID());
        mensagemNova.setConteudo("ABC 12345");

        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagemAntiga));

        //Act e Assert
        assertThatThrownBy(()-> mensagemService.alterarMensagem(id,mensagemNova))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("mensagem não apresenta o ID correto");
        verify(mensagemRepository,times(1)).findById(any(UUID.class));
        verify(mensagemRepository,never()).save(any(Mensagem.class));

    }

    @Test
    void devePermitirRemoverMensagem(){
        // Arrange
        var id = UUID.fromString("e0b73fc9-cca5-4173-bb0d-1d7813984f3f");
        var mensagem = MensagemHelper.gerarMensagem();
        mensagem.setId(id);
        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagem));
        doNothing().when(mensagemRepository).deleteById(id);
        //Act
        var mensagemFoiRemovida = mensagemService.removerMensagem(id);
        //Assert
        assertThat(mensagemFoiRemovida).isTrue();
        verify(mensagemRepository,times(1)).findById(any(UUID.class));
        verify(mensagemRepository,times(1)).delete(any(Mensagem.class));


        //fail("Teste não implementado");
    }

    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste(){

        // Arrange
        var id = UUID.fromString("e0b73fc9-cca5-4173-bb0d-1d7813984f4f");
        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());
       // Act e Assert
        assertThatThrownBy(()-> mensagemService.removerMensagem(id) )
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem não encontrada");
        verify(mensagemRepository,times(1)).findById(any(UUID.class));
        verify(mensagemRepository,never()).deleteById(any(UUID.class));


    }

    @Test
    void devePermitirListarMensagens(){
       // Arrange
        Page<Mensagem> listaDeMensagens = new  org.springframework.data.domain.PageImpl<>(Arrays.asList(
                MensagemHelper.gerarMensagem(),
                MensagemHelper.gerarMensagem()
        ));
        when(mensagemRepository.listarMensagens(any(Pageable.class)))
                .thenReturn(listaDeMensagens);

        // Action
        var resultadoObtido = mensagemService.listarMensagens(Pageable.unpaged());

        // Assert
        assertThat(resultadoObtido).hasSize(2);
        assertThat(resultadoObtido.getContent()).asList()
                .allSatisfy( mensagem -> {
                    assertThat(mensagem)
                            .isNotNull()
                            .isInstanceOf(Mensagem.class);
                        } );
        verify(mensagemRepository,times(1)).listarMensagens(any(Pageable.class));

    }


}
