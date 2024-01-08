package br.com.arlei.service;

import br.com.arlei.exception.MensagemNotFoundException;
import br.com.arlei.model.Mensagem;
import br.com.arlei.repository.MensagemRepository;
import br.com.arlei.utils.MensagemHelper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class MensagemServiceIT {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private MensagemService mensagemService;

    // replicar os os mesmos testes unitarios

    /**
     * Nested com as classes para organizar os contextos dos testes
     */

    @Nested
    class registrarMensagem{

        @Test
        void devePermitirRegistrarMensagem()
        {
            var mensagem = MensagemHelper.gerarMensagem();

            var mensagemArmazenada = mensagemService.registrarMensagem(mensagem);

            assertThat(mensagemArmazenada)
                    .isNotNull()
                    .isInstanceOf(Mensagem.class);
            assertThat(mensagemArmazenada.getId())
                    .isNotNull();
            assertThat(mensagemArmazenada.getUsuario())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(mensagem.getUsuario());
            assertThat(mensagemArmazenada.getConteudo())
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(mensagem.getConteudo());
        }


    }

    @Nested
    class listarMensagem{

        @Test
        void devePermitirListarMensagens(){

            Page<Mensagem> mensagens = mensagemService.listarMensagens(Pageable.unpaged());

            assertThat(mensagens).hasSize(3);
            assertThat(mensagens.getContent())
                    .asList()
                    .allSatisfy(mensagem -> {
                        assertThat(mensagem).isNotNull();
                        assertThat(mensagem).isInstanceOf(Mensagem.class);
                    });
            //fail("Teste não implementado");

        }

        @Test
        void devePermitirBuscarMensagem(){
            // arrange
            var mensagem = MensagemHelper.gerarMensagem();
            //act
            var resultadoObtido = mensagemService.registrarMensagem(mensagem);
            // assert
            assertThat(resultadoObtido)
                    .isNotNull()
                    .isInstanceOf(Mensagem.class);
            assertThat(resultadoObtido.getId()).isNotNull();
            assertThat(resultadoObtido.getDataCriacao()).isNotNull();
            assertThat(resultadoObtido.getGostei()).isZero();

        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste(){

            var id = UUID.fromString("4106c507-28d5-4294-97fd-3c025d83cb31");
            // ACT e Assert
            assertThatThrownBy(() -> mensagemService.buscarMensagem(id))
                    .isInstanceOf(MensagemNotFoundException.class);
        }

    }

    @Nested
    class alterarMensagem{
        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste(){

            var id = UUID.fromString("5f789b39-4295-42c1-a65b-cfca5b987db2");
            var mensagemNova = MensagemHelper.gerarMensagem();

            assertThatThrownBy(
                    () -> mensagemService.alterarMensagem(id, mensagemNova))
                    .isInstanceOf(MensagemNotFoundException.class)
                    .hasMessage("Mensagem não encontrada");

            //fail("Teste não implementado");
        }

        @Test
        void devePermitirAlterarMensagem(){

            UUID id = UUID.fromString("4106c507-28d5-4294-97fd-3c025d83cb30");
            var mensagemAtualizada = MensagemHelper.gerarMensagem();
            mensagemAtualizada.setId(id);

            var resultadoObtido = mensagemService.alterarMensagem(id,mensagemAtualizada);

            assertThat(resultadoObtido.getId()).isEqualTo(mensagemAtualizada.getId());
            assertThat(resultadoObtido.getUsuario()).isNotEqualTo(mensagemAtualizada.getUsuario());
            assertThat(resultadoObtido.getConteudo()).isEqualTo(mensagemAtualizada.getConteudo());
            //fail("Teste não implementado");
        }
        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente(){


            var id = UUID.fromString("4106c507-28d5-4294-97fd-3c025d83cb30");
            var mensagemNova = MensagemHelper.gerarMensagemCompleta();

            assertThatThrownBy(
                    () -> mensagemService.alterarMensagem(id, mensagemNova))
                    .isInstanceOf(MensagemNotFoundException.class)
                    .hasMessage("mensagem atualizada não apresenta o ID correto");
            //    fail("Teste não implementado");

        }

    }

    @Nested
    class removerMensagem{
        @Test
        void devePermitirRemoverMensagem(){

            var id = UUID.fromString("4106c509-28d5-4294-97fd-3c025d83cb30");
            var resultado = mensagemService.removerMensagem(id);
            assertThat(resultado).isTrue();

            //fail("Teste não implementado");

        }
        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste(){


            fail("Teste não implementado");

        }

    }



}
