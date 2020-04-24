package com.osworks.api.controller;

import com.osworks.api.domain.model.OrdemServico;
import com.osworks.api.domain.model.OrdemServicoInput;
import com.osworks.api.domain.repository.OrdemServicoRepository;
import com.osworks.api.domain.service.GestaoOrdemServicos;
import com.osworks.api.model.OrdemServicoModel;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ordens-servico")
public class OrdemServicoController {

    private final GestaoOrdemServicos gestaoOrdemServicos;

    private final OrdemServicoRepository ordemServicoRepository;

    private final ModelMapper modelMapper;

    public OrdemServicoController(GestaoOrdemServicos gestaoOrdemServicos, OrdemServicoRepository ordemServicoRepository, ModelMapper modelMapper) {
        this.gestaoOrdemServicos = gestaoOrdemServicos;
        this.ordemServicoRepository = ordemServicoRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
        OrdemServico ordemServico = toEntity(ordemServicoInput);
        return toModel(gestaoOrdemServicos.criarOrdemServico(ordemServico));
    }

    @GetMapping
    public List<OrdemServicoModel> listar() {
        return toColletionsModel(ordemServicoRepository.findAll());
    }

    @GetMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoModel> buscar(@PathVariable Long ordemServicoId) {
        Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(ordemServicoId);

        if (ordemServico.isPresent()) {
            OrdemServicoModel model = toModel(ordemServico.get());
            return ResponseEntity.ok(model);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("{ordemServicoId}/finalizacao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finalizar(@PathVariable Long ordemServicoId) {
        gestaoOrdemServicos.finalizar(ordemServicoId);
    }

    private OrdemServicoModel toModel(OrdemServico ordemServico) {
        return modelMapper.map(ordemServico, OrdemServicoModel.class);
    }

    private List<OrdemServicoModel> toColletionsModel(List<OrdemServico> ordemServicos) {
        return ordemServicos.stream()
                .map(ordemServico -> toModel(ordemServico))
                .collect(Collectors.toList());
    }

    private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
        return modelMapper.map(ordemServicoInput, OrdemServico.class);
    }
}
