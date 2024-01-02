package br.com.arlei.service;

import br.com.arlei.repository.MensagemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class MensagemServiceIT {

    @Autowired
    private MensagemRepository mensagemRepository;

    private MensagemService mensagemService;

    // replicar os os mesmos testes unitarios

    @Test
    void devePermitirBuscarMensagem(){ fail("Teste não implementado");}
    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste(){ fail("Teste não implementado");}
    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste(){ fail("Teste não implementado");}
    @Test
    void devePermitirRegistrarMensagem()
    {
        fail("Teste não implementado");
    }
    @Test
    void devePermitirAlterarMensagem(){ fail("Teste não implementado");}
    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdDaMensagemNovaApresentaValorDiferente(){ fail("Teste não implementado");}
    @Test
    void devePermitirRemoverMensagem(){ fail("Teste não implementado");}
    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste(){ fail("Teste não implementado");}
    @Test
    void devePermitirListarMensagens(){ fail("Teste não implementado");}

}
