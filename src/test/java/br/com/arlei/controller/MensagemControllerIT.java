package br.com.arlei.controller;

import br.com.arlei.model.Mensagem;
import br.com.arlei.utils.MensagemHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.fail;


// Criar uma porta aleatória para o spring boot
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class MensagemControllerIT {

    // Capturar a porta aleatória
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        // para mostrar o log de erro quando falhar senão o log.All ou o esquema abaixo ja demonstra....
        // RestAssured.filters(new AllureRestAssured()); // desta forma como estamos utilizando nested class gera informação duplicada
    }

    @Nested
    class RegistrarMensagem {

        @Test
        void devePermitirRegistarMensagem() {

            var mensagem = MensagemHelper.gerarMensagem();

            // Biblioteca do Rest Assured para testar de forma integrada o Controller, tratar o exception por conflito no POM xml
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mensagem)
                    .log().all()
            .when()
                    .post("/mensagens")

            .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    // aqui usamos o schema e a configuração do pom io.rest-assured  \ json-schema-validator
                    // Vaida o retorno dos campos do Json
                    .body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"));
                    // Meu json não esta valido, ajustar....



        }

        @Test
        void deveGerarExcecao_QuandoRegistrarMensagem_PayloadComXml() {
            String xmlPayload = "<mensagem><usuario>John</usuario><conteudo>Conteúdo da mensagem</conteudo></mensagem>";

            // Biblioteca do Rest Assured, tratar o exception por conflito no POM xml
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(xmlPayload)
                    //.log().all()
             .when()
                    .post("/mensagens")

             .then()
                    //.log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    /* posso criar um jso do erro no esquema do ok... e usar ele na validação...
                    .body("$", hasKey("timestamp"))
                    .body("$", hasKey("status"))
                    .body("$", hasKey("error"))
                    .body("$", hasKey("path"))

                     */

                    .body("error", equalTo("Bad Request"));


        }
    }

    @Nested
    class BuscarMensagem{

        @Test
        void devePermitirBuscarMensagem(){
            //Usar um codigo valido do nosso banco...

            var id = "4106c507-28d5-4294-97fd-3c025d83cb30";
            when()
                    .get("/mensagens/{id}",id)
                            .then()
                                    .statusCode(HttpStatus.OK.value());


            //fail("teste não implementado");

        }
        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste(){

            var id = "4106c507-28d5-4294-97fd-3c025d83cb36";
            when()
                    .get("/mensagens/{id}",id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());

        }


    }
    @Nested
    class AlterarMensagem{
        @Test
        void devePermitirAlterarMensagem(){

            var id = UUID.fromString("4106c508-28d5-4294-97fd-3c025d83cb30");
            var timestamp = LocalDateTime.now();
            var mensagem = Mensagem.builder()
                    .id(id)
                    .usuario("Eva")
                    .conteudo("Conteudo da Mensagem")
                    .dataCriacao(timestamp)
                    .dataAlteracao(timestamp)
                    .build();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mensagem)
            .when()
                    .put("/mensagens/{id}",id)
            .then()
                    .statusCode(HttpStatus.ACCEPTED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/mensagem.schema.json"));
            //fail("teste não implementado");

        }
        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_PayloadComXml(){ fail("teste não implementado");}
        @Test
        void deveGerarExcecao_QuandoAlterarMenssagem_IdNaoExiste(){ fail("teste não implementado");}
        @Test
        void deveGerarExcecao_QuandoAlterarMenssagem_IdMensagemNovaApresentaValorDiferente(){ fail("teste não implementado");}

    }
    @Nested
    class RemoverMensagem{
        @Test
        void devePermitirRemoverMensagem(){ fail("teste não implementado");}

        @Test
        void deveGerarExcecao_QuandoIdNaoExiste(){ fail("teste não implementado");}



    }

    @Nested
    class ListarMensagem{

        @Test
        void devePermitirListarMensagens(){fail("teste não implementado");}
    }
}
