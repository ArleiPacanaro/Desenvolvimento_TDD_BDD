package br.com.arlei.controller;

import br.com.arlei.exception.MensagemNotFoundException;
import br.com.arlei.model.Mensagem;
import br.com.arlei.service.MensagemService;
import br.com.arlei.utils.MensagemHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MensagemControllerTest {

    // Para efetuar as requisições
    private MockMvc mockMvc;

    @Mock
    private MensagemService mensagemService;

    AutoCloseable mock;

    // Antes de cada teste iniciar
    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        MensagemController mensagemController = new MensagemController(mensagemService);
        // definindo o controle do nosso mockmvc

        mockMvc = MockMvcBuilders.standaloneSetup(mensagemController)
                  .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();

    }

    // Após cada teste limpar da memória
    @AfterEach
    void tearDown() throws Exception {
        mock.close();

    }

    @Nested
    class RegistrarMensagem{

        @Test
        void devePermitirRegistrarMensagem() throws Exception {
            //Arrange
            var mensagem = MensagemHelper.gerarMensagem();
            when(mensagemService.registrarMensagem(any(Mensagem.class)))
                    .thenAnswer(i -> i.getArgument(0));
            //Action

            mockMvc.perform(MockMvcRequestBuilders.post("/mensagens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
                    .andExpect(status().isCreated());

            // Assert
            verify(mensagemService,times(1)).registrarMensagem(any(Mensagem.class));


        }
        @Test
        void deveGerarExcecao_QuandoRegistrarMensagem_PayloadComXml() throws Exception {
            String xmlPayload
                    = "<mensagem><usuario>John</usuario><conteudo>Conteúdo da mensagem</conteudo></mensagem>";

            mockMvc.perform(post("/mensagens")
                            .contentType(MediaType.APPLICATION_XML)
                            .content(xmlPayload))
                    .andExpect(status().isUnsupportedMediaType());
            verify(mensagemService, never()).registrarMensagem(any(Mensagem.class));
        }

    }

    @Nested
    class BuscarMensagem{
        @Test
        void devePermitirBuscarMensagem() throws Exception {
            var id = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002");
            var mensagem = MensagemHelper.gerarMensagem();
            mensagem.setId(id);
            mensagem.setDataCriacao(LocalDateTime.now());

            when(mensagemService.buscarMensagem(any(UUID.class))).thenReturn(mensagem);

            mockMvc.perform(get("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(mensagem.getId().toString()))
                    .andExpect(jsonPath("$.conteudo").value(mensagem.getConteudo()))
                    .andExpect(jsonPath("$.usuario").value(mensagem.getUsuario()))
                    .andExpect(jsonPath("$.dataCriacao").exists())
                    .andExpect(jsonPath("$.gostei").exists());
            verify(mensagemService, times(1)).buscarMensagem(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() throws Exception {
            var id = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002");

            when(mensagemService.buscarMensagem(any(UUID.class)))
                    .thenThrow(new MensagemNotFoundException("mensagem não encontrada"));

            mockMvc.perform(get("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
            verify(mensagemService, times(1))
                    .buscarMensagem(any(UUID.class));
        }

    }

    @Nested
    class AlterarMensagem{

        @Test
        void devePermitirAlterarMensagem() throws Exception {

            var id = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002");
            var mensagem = MensagemHelper.gerarMensagem();
            mensagem.setId(id);

            when(mensagemService.alterarMensagem(any(UUID.class), any(Mensagem.class)))
                    .thenAnswer(i -> i.getArgument(1));

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(mensagem.getId().toString()))
                    .andExpect(jsonPath("$.conteudo").value(mensagem.getConteudo()))
                    .andExpect(jsonPath("$.usuario").value(mensagem.getUsuario()))
                    .andExpect(jsonPath("$.dataCriacao").value(mensagem.getDataCriacao()))
                    .andExpect(jsonPath("$.gostei").value(mensagem.getGostei()));
            verify(mensagemService, times(1))
                    .alterarMensagem(any(UUID.class), any(Mensagem.class));

        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_PayloadComXml() throws Exception {

            var id = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002");
            String xmlPayload = "<mensagem><usuario>John</usuario><conteudo>Conteúdo da mensagem</conteudo></mensagem>";

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_XML)
                            .content(xmlPayload))
                    .andExpect(status().isUnsupportedMediaType());
            verify(mensagemService, never()).alterarMensagem(any(UUID.class), any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMenssagem_IdNaoExiste() throws Exception {
            var id = "259bdc02-1ab5-11ee-be56-0242ac120002";
            var mensagemRequest = MensagemHelper.gerarMensagem();

            when(mensagemService.alterarMensagem(any(UUID.class), any(Mensagem.class)))
                    .thenThrow(new MensagemNotFoundException("mensagem não apresenta o ID correto"));

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagemRequest)))
                            .andExpect(status().isNotFound())
                    // podemos ter um print que nos apresenta como foi toda a requisição... amas apenas em testes não na pipeline pelo volume de mensagens
                            .andExpect(content().string("mensagem não apresenta o ID correto"));
            verify(mensagemService, never()).removerMensagem(any(UUID.class));
            verify(mensagemService, times(1)).alterarMensagem(any(UUID.class), any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMenssagem_IdMensagemNovaApresentaValorDiferente() throws Exception {

            var id = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002");
            var mensagemRequest = MensagemHelper.gerarMensagem();
            mensagemRequest.setId(UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120000"));


            when(mensagemService.alterarMensagem(any(UUID.class), any(Mensagem.class)))
                    .thenThrow(new MensagemNotFoundException("mensagem não apresenta o ID correto"));

            mockMvc.perform(put("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagemRequest)))
                    .andExpect(status().isNotFound())
                    // podemos ter um print que nos apresenta como foi toda a requisição... amas apenas em testes não na pipeline pelo volume de mensagens
                    .andExpect(content().string("mensagem não apresenta o ID correto"));
            verify(mensagemService, never()).removerMensagem(any(UUID.class));
            verify(mensagemService, times(1)).alterarMensagem(any(UUID.class), any(Mensagem.class));

        }

    }

    @Nested
    class RemoverMensagem{
        @Test
        void devePermitirRemoverMensagem() throws Exception {

            var id = UUID.fromString("259bdc02-1ab5-11ee-be56-0242ac120002");
            when(mensagemService.removerMensagem(any(UUID.class)))
                    .thenReturn(true);

            mockMvc.perform(delete("/mensagens/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().string("mensagem removida"));
            verify(mensagemService, times(1))
                    .removerMensagem(any(UUID.class));
        }
        @Test
        void deveGerarExcecao_QuandoIdNaoExiste() throws Exception {
            var id = "2";

            mockMvc.perform(delete("/mensagens/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("ID inválido"));
            verify(mensagemService, never())
                    .removerMensagem(any(UUID.class));

        }

    }

    @Nested
    class ListarMensagem {
        @Test
        void devePermitirListarMensagens() throws Exception {

            var mensagem = MensagemHelper.gerarMensagemCompleta();
            Page<Mensagem> page = new PageImpl<>(Collections.singletonList(
                    mensagem
            ));
            when(mensagemService.listarMensagens(any(Pageable.class)))
                    .thenReturn(page);
            mockMvc.perform(get("/mensagens")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(mensagem.getId().toString()))
                    .andExpect(jsonPath("$.content[0].conteudo").value(mensagem.getConteudo()))
                    .andExpect(jsonPath("$.content[0].usuario").value(mensagem.getUsuario()))
                    .andExpect(jsonPath("$.content[0].dataCriacao").exists())
                    .andExpect(jsonPath("$.content[0].gostei").exists());
            verify(mensagemService, times(1))
                    .listarMensagens(any(Pageable.class));

        }
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
