package com.osworks.api.controller;

import com.osworks.api.domain.model.Cliente;
import com.osworks.api.domain.repository.ClienteRepository;
import com.osworks.api.domain.service.CadastroClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
public class ClienteController {

    private final CadastroClienteService cadastroCliente;

    public ClienteController(CadastroClienteService cadastroCliente) {
        this.cadastroCliente = cadastroCliente;
    }

    @GetMapping
    public List<Cliente> listar() {
        return cadastroCliente.findAll();
    }

    @GetMapping("/name/{nome}")
    public List<Cliente> buscar(@PathVariable String nome) {
        return cadastroCliente.findByNomeContaining(nome);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
        Optional<Cliente> cliente = cadastroCliente.findById(clienteId);

        if (cliente.isPresent()) return ResponseEntity.ok().body(cliente.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
        return cadastroCliente.save(cliente);
    }

    @PutMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long clienteId, @RequestBody Cliente cliente) {

        if (!cadastroCliente.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        cliente.setId(clienteId);
        cliente = cadastroCliente.save(cliente);

        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> remover(@PathVariable Long clienteId) {
        if (!cadastroCliente.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
