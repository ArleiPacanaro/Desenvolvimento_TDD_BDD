package br.com.arlei.controller;

import java.util.UUID;
import br.com.arlei.exception.MensagemNotFoundException;
import br.com.arlei.model.Mensagem;
import br.com.arlei.service.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Classe controladora da mensagem.
 */
@RestController
@RequestMapping("mensagens")
@RequiredArgsConstructor
public class MensagemController {

  private final MensagemService mensagemService;


  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Mensagem> registrarMensagem(@RequestBody  Mensagem mensagem){
    var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);
    return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
  }

  @GetMapping(
          value = "/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> buscarMensagem(@PathVariable String id) {
    try {
      var uuid = UUID.fromString(id);
      var mensagemEncontrada = mensagemService.buscarMensagem(uuid);
      return new ResponseEntity<>(mensagemEncontrada, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("ID inválido");
    } catch (MensagemNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @PutMapping(
          value = "/{id}",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> atualizarMensagem(
          @PathVariable String id,
          @RequestBody Mensagem mensagem) {

    try {
      var uuid = UUID.fromString(id);
      var mensagemAtualizada = mensagemService.alterarMensagem(uuid, mensagem);
      return new ResponseEntity<>(mensagemAtualizada, HttpStatus.ACCEPTED);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("ID inválido");
    } catch (MensagemNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> apagarMensagem(@PathVariable String id) {

    try {
      var uuid = UUID.fromString(id);
      mensagemService.removerMensagem(uuid);
      return new ResponseEntity<>("mensagem removida", HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("ID inválido");
    } catch (MensagemNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
  @GetMapping(
          value = "",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Mensagem>> listarMensagens(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Mensagem> mensagens = mensagemService.listarMensagens(pageable);
    return new ResponseEntity<>(mensagens, HttpStatus.OK);
  }
}
