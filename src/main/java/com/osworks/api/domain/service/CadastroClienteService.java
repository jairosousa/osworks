package com.osworks.api.domain.service;

import com.osworks.api.domain.exception.NegocioException;
import com.osworks.api.domain.model.Cliente;
import com.osworks.api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroClienteService {

    private final ClienteRepository clienteRepository;

    public CadastroClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente save(Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
        if (clienteExistente != null && !clienteExistente.equals(cliente)) {
            throw new NegocioException("Já existe cliente cadastrado com este e-mail");
        }
        return clienteRepository.save(cliente);
    }

    public void excluir(Long clienteId) {
        clienteRepository.deleteById(clienteId);
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public List<Cliente> findByNomeContaining(String nome) {
        return clienteRepository.findByNomeContaining(nome);
    }

    public Optional<Cliente> findById(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public boolean existsById(Long clienteId) {
        return clienteRepository.existsById(clienteId);
    }
}
