package swagger.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import swagger.model.Cliente;
import swagger.service.ClienteService;

// Controller responsavel pelos endpoints relacionados aos clientes
@RestController
// Define /api/clientes como caminho base dos endpoints desta classe
@RequestMapping("/api/clientes")
// Permite que a API receba requisições de diferentes origens
@CrossOrigin(origins = "*")
public class ClienteController {

    // Injeta o serviço responsável pelas regras de negócio dos clientes
    @Autowired
    private ClienteService clienteService;
    // Retorna todos os clientes cadastrados
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.findAll();
    }



    // Busca um cliente específico pelo seu ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.findById(id);

        // Retorna 200 caso encontre o cliente ou 404 caso não encontre
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Busca um cliente utilizando o endereço de e-mail
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> getClienteByEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteService.findByEmail(email);

        // Retorna o cliente encontrado ou 404 caso não exista
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Permite pesquisar clientes pelo nome
    @GetMapping("/search")
    public List<Cliente> searchClientes(@RequestParam String nome) {
        return clienteService.findByNome(nome);
    }


    // Cadastra um novo cliente
    // @Valid aplica as validações definidas na entidade Cliente
    // @RequestBody recebe os dados enviados no corpo da requisição
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente savedCliente = clienteService.save(cliente);
            return ResponseEntity.ok(savedCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Atualiza os dados de um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteDetails) {
        try {
            Cliente updatedCliente = clienteService.update(id, clienteDetails);
            return ResponseEntity.ok(updatedCliente);
        } catch (RuntimeException e) {

            // Retorna 404 caso o cliente não seja encontrado
            return ResponseEntity.notFound().build();
        }
    }

    // Remove um cliente pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
