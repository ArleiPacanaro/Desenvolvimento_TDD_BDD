package br.com.arlei.service;

import br.com.arlei.exception.MensagemNotFoundException;
import br.com.arlei.model.Mensagem;
import br.com.arlei.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService{



    private final MensagemRepository mensagemRepository;


    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return mensagemRepository.findById(id).orElseThrow(() -> new MensagemNotFoundException("Mensagem não encontrada"));
    }

    @Override
    public Mensagem alterarMensagem(UUID id, Mensagem mensagemNova) {
        var mensagem = buscarMensagem(id);
        if(!mensagem.getId().equals(mensagemNova.getId())){
            throw new MensagemNotFoundException("mensagem atualizada não apresenta o ID correto");
        }
        mensagem.setConteudo(mensagemNova.getConteudo());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Boolean removerMensagem(UUID id) {
        var mensagem = buscarMensagem(id);
        mensagemRepository.deleteById(id);
        return true;
    }

  @Override
  public Page<Mensagem> listarMensagens(Pageable page) {
     return mensagemRepository.listarMensagens(page);
 }
}
