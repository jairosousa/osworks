package com.osworks.api.domain.service;

import com.osworks.api.domain.exception.EntidadeNaoEncontradaException;
import com.osworks.api.domain.exception.NegocioException;
import com.osworks.api.domain.model.Cliente;
import com.osworks.api.domain.model.Comentario;
import com.osworks.api.domain.model.OrdemServico;
import com.osworks.api.domain.model.StatusOrdemServico;
import com.osworks.api.domain.repository.ClienteRepository;
import com.osworks.api.domain.repository.ComentarioRepository;
import com.osworks.api.domain.repository.OrdemServicoRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class GestaoOrdemServicos {

    public final OrdemServicoRepository ordemServicoRepository;

    public final ClienteRepository clienteRepository;

    public final ComentarioRepository comentarioRepository;

    public GestaoOrdemServicos(OrdemServicoRepository ordemServicoRepository, ClienteRepository clienteRepository, ComentarioRepository comentarioRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public OrdemServico criarOrdemServico(OrdemServico ordemServico) {

        Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
                .orElseThrow(() -> new NegocioException("Cliente não encontrado"));

        ordemServico.setCliente(cliente);
        ordemServico.setStatus(StatusOrdemServico.ABERTA);
        ordemServico.setDataAbertura(OffsetDateTime.now());

        return ordemServicoRepository.save(ordemServico);
    }
    
    public void finalizar(Long ordemServicoId) {

        OrdemServico ordemServico = buscar(ordemServicoId);

        ordemServico.finalizar();

        ordemServicoRepository.save(ordemServico);
    }

    public Comentario adicionarComentario(Long ordemServicoId, String descricao) {

        OrdemServico ordemServico = buscar(ordemServicoId);

        Comentario comentario = new Comentario();
        comentario.setDataEnvio(OffsetDateTime.now());
        comentario.setDescricao(descricao);
        comentario.setOrdemServico(ordemServico);

        return comentarioRepository.save(comentario);

    }

    private OrdemServico buscar(Long ordemServicoId) {
        return ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrado"));
    }
}
