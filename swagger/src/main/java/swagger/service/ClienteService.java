package swagger.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swagger.model.Cliente;
import swagger.repository.ClienteRepository;

// Camada responsável pelas regras de negócio relacionadas aos clientes
@Service
public class ClienteService {

    // Repository utilizado para acessar os clientes no banco
    @Autowired
    private ClienteRepository clienteRepository;

    // Retorna todos os clientes
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    // Busca um cliente pelo ID
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    // Busca um cliente pelo e-mail
    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    // Pesquisa clientes pelo nome
    public List<Cliente> findByNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Executa o cadastro de um novo cliente dentro de uma transação
    @Transactional
    public Cliente save(Cliente cliente) {
        // Impede o cadastro de dois clientes utilizando o mesmo e-mail
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }

    // Atualiza os dados de um cliente existente
    @Transactional
    public Cliente update(Long id, Cliente clienteDetails) {

        // Busca o cliente que será atualizado
        // Caso não exista, interrompe a operação
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
        
        // Verifica se o email já existe em outro cliente
        if (!cliente.getEmail().equals(clienteDetails.getEmail()) && 
            clienteRepository.existsByEmail(clienteDetails.getEmail())) {
            throw new RuntimeException("Já existe um cliente com este email: " + clienteDetails.getEmail());
        }
        
        cliente.setNome(clienteDetails.getNome());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefone(clienteDetails.getTelefone());
        cliente.setDataNascimento(clienteDetails.getDataNascimento());
        
        return clienteRepository.save(cliente);
    }

    // Exclui um cliente após verificar se existem pedidos associados
    @Transactional
    public void deleteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
        
        // Verifica se o cliente tem pedidos antes de deletar
        if (!cliente.getPedidos().isEmpty()) {
            throw new RuntimeException("Não é possível excluir cliente com pedidos associados");
        }
        
        clienteRepository.deleteById(id);
    }

    // Verifica se já existe um cliente com o e-mail informado
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
}