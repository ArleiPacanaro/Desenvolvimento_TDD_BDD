package br.com.arlei.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {
    @Id
    private UUID id;

    @Column(nullable = false)
    @NotEmpty(message = "Usuário não pode estar vazio")
    private String usuario;


    @Column(nullable = false)
    @NotEmpty(message = "conteúdo não pode estar vazio")
    private String conteudo;

    // Ja será criado de forma automática
    @Builder.Default
    private LocalDateTime dataCriacaoMensagem = LocalDateTime.now();

    @Builder.Default
    private int gostei = 0;



}