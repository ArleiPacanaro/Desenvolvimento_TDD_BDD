package service;

import br.com.arlei.exception.MensagemNotFoundException;
import br.com.arlei.model.Mensagem;
import br.com.arlei.repository.MensagemRepository;
import br.com.arlei.service.MensagemService;
import br.com.arlei.service.MensagemServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


class MensagemServiceTest {

    private MensagemService mensagemService;

    @Mock
    private  MensagemRepository mensagemRepository;

    // Trabalhar com Fake - Mockito.
    AutoCloseable mock;
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
    public void devePermitirRegistrarMensagem()
    {
        // conceito do triple A
        // Arrange
        var mensagem = gerarMensagem();

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
        var mensagem = gerarMensagem();
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

        // Arrange
       var id = UUID.randomUUID();

       var mensagemAntigo = gerarMensagem();
       mensagemAntigo.setId(id);


       var mensagemNova = new Mensagem();
       mensagemNova.setId(mensagemAntigo.getId());
       mensagemNova.setUsuario(mensagemAntigo.getUsuario());
       mensagemNova.setDataCriacaoMensagem(mensagemAntigo.getDataCriacaoMensagem());
       mensagemNova.setGostei(mensagemAntigo.getGostei());
       mensagemNova.setConteudo("ABCD 12345");


       when(mensagemRepository.findById(id))
               .thenReturn(Optional.of(mensagemAntigo));

       when(mensagemRepository.save(mensagemNova))
               .thenAnswer(i-> i.getArgument(0));

       // Act


        var mensagemObtida = mensagemService.alterarMensagem(id,mensagemNova);

        // Assert

        assertThat(mensagemObtida.getId()).isEqualTo(mensagemAntigo.getId());

        verify(mensagemRepository,times(1)).findById(any(UUID.class));
        verify(mensagemRepository,times(1)).save(any(Mensagem.class));


    }

    @Test
    void devePermitirRemoverMensagem(){
        fail("Teste não implementado");
    }

    @Test
    void devePermitirListarMensagens(){
        fail("Teste não implementado");
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
