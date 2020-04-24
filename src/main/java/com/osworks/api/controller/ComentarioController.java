package com.osworks.api.controller;

import com.osworks.api.domain.exception.EntidadeNaoEncontradaException;
import com.osworks.api.domain.model.Comentario;
import com.osworks.api.domain.model.ComentarioInput;
import com.osworks.api.domain.model.ComentarioModel;
import com.osworks.api.domain.model.OrdemServico;
import com.osworks.api.domain.repository.OrdemServicoRepository;
import com.osworks.api.domain.service.GestaoOrdemServicos;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

    private final GestaoOrdemServicos gestaoOrdemServicos;

    private final OrdemServicoRepository ordemServicoRepository;

    private final ModelMapper modelMapper;

    public ComentarioController(GestaoOrdemServicos gestaoOrdemServicos, OrdemServicoRepository ordemServicoRepository, ModelMapper modelMapper) {
        this.gestaoOrdemServicos = gestaoOrdemServicos;
        this.ordemServicoRepository = ordemServicoRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ComentarioModel> listar(@PathVariable Long ordemServicoId) {
        OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));

        return toColletionsModel(ordemServico.getComentarios());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioModel adicionar(@PathVariable Long ordemServicoId,
                                     @RequestBody ComentarioInput comentarioInput) {
        Comentario comentario = gestaoOrdemServicos.adicionarComentario(ordemServicoId, comentarioInput.getDescricao());

        return toModel(comentario);
    }

    private ComentarioModel toModel(Comentario comentario) {
        return modelMapper.map(comentario, ComentarioModel.class);
    }

    private List<ComentarioModel> toColletionsModel(List<Comentario> comentarios) {
        return comentarios.stream()
                .map(comentario -> toModel(comentario))
                .collect(Collectors.toList());
    }
}
