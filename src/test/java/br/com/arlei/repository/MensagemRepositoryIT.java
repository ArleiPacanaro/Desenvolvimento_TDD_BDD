package br.com.arlei.repository;

import br.com.arlei.model.Mensagem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

// Ja a classe para interagir com o baoc de dados...
@SpringBootTest
@AutoConfigureTestDatabase
public class MensagemRepositoryIT {

   @Autowired
   private MensagemRepository mensagemRepository;


   @Test
   void devePermitirCriarTabela(){
       var totalDeRegistro = mensagemRepository.count();
       assertThat(totalDeRegistro).isNotNegative();

   }



    @Test
    void devePermitirRegistrarMensagem(){

       //Arrange

       var id = UUID.randomUUID();
       var mensagem = gerarMensagem();
       mensagem.setId(id);

       //Act
        var mensagemRecebida = mensagemRepository.save(mensagem);

        //Assert

        assertThat(mensagemRecebida)
                .isInstanceOf(Mensagem.class)
                        .isNotNull();
        assertThat(mensagemRecebida.getId()).isEqualTo(mensagem.getId());
        assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagem.getUsuario());




        //fail("teste não implementado");

    }

    @Test
    void devePermitirRemoverMensagem(){

       // Arrange
        var id = UUID.fromString("4106c508-28d5-4294-97fd-3c025d83cb30");

        // Act
        mensagemRepository.deleteById(id);
        Optional<Mensagem> mensagemRecebidaOpcional = mensagemRepository.findById(id);

        // Assert

        assertThat(mensagemRecebidaOpcional).isEmpty();

       // fail("teste não implementado");
    }

    @Test
    void devePermitirListarMensagem(){
       //Act
       var resultadosObtidos = mensagemRepository.findAll();

       //Arrange
        assertThat(resultadosObtidos).hasSizeGreaterThan(0);

       // fail("teste não implementado");

    }

    @Test
    void devePermitirBuscarMensagem(){

       /* comentamos, pois vamos usar a mensgem criada via script, caso fosse pelo nosso insert o, tirar os comentarios
       var id = UUID.randomUUID();
       var mensagem = gerarMensagem();
       mensagem.setId(id);
       registrarMensagem(mensagem);
       */

        // utilizando o 1 caso do script
        var id = UUID.fromString("4106c507-28d5-4294-97fd-3c025d83cb30");

       // Act
        Optional<Mensagem> mensagemRecebidaOpcional = mensagemRepository.findById(id);

        // Assert
        assertThat(mensagemRecebidaOpcional).isPresent();


        mensagemRecebidaOpcional.ifPresent( mensagemRecebida -> {
            assertThat(mensagemRecebida.getId()).isEqualTo(id);
            //assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
            //assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagem.getUsuario());
        });



        //fail("teste não implementado");

    }

    private Mensagem registrarMensagem(Mensagem mensagem){

       return mensagemRepository.save(mensagem);

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
