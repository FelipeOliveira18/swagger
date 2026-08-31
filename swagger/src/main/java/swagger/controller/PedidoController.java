package swagger.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import swagger.model.Pedido;
import swagger.service.PedidoService;

// Controller responsável pelas operações relacionadas aos pedidos
@RestController
// Define o caminho base dos endpoints de pedidos
@RequestMapping("/api/pedidos")
// Permite requisições vindas de diferentes origens
@CrossOrigin(origins = "*")
public class PedidoController {

    // Serviço responsável pelas regras de negócio dos pedidos
    @Autowired
    private PedidoService pedidoService;

    // Retorna todos os pedidos cadastrados
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.findAll();
    }

    // Busca um pedido pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.findById(id);

        // Retorna o pedido encontrado ou 404 caso ele não exista
        return pedido.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    // Retorna todos os pedidos pertencentes a um determinado cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> getPedidosByCliente(@PathVariable Long clienteId) {
        return pedidoService.findByClienteId(clienteId);
    }
    // Filtra pedidos de acordo com o status informado
    @GetMapping("/status/{status}")
    public List<Pedido> getPedidosByStatus(@PathVariable String status) {
        return pedidoService.findByStatus(status);
    }

    // Busca pedidos realizados dentro de um intervalo de datas
    @GetMapping("/periodo")
    public List<Pedido> getPedidosByPeriodo(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return pedidoService.findByPeriodo(inicio, fim);
    }

    // Calcula e retorna o valor total de um pedido
    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> getTotalPedido(@PathVariable Long id) {
        try {
            BigDecimal total = pedidoService.calcularTotalPedido(id);
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            // Retorna 404 caso o pedido não seja encontrado
            return ResponseEntity.notFound().build();
        }
    }

    // Cria um novo pedido
    @PostMapping
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) {
        try {
            Pedido savedPedido = pedidoService.save(pedido);
            return ResponseEntity.ok(savedPedido);
        } catch (RuntimeException e) {

            // Retorna 400 quando os dados do pedido não são aceitos
            return ResponseEntity.badRequest().build();
        }
    }

    // Atualiza somente o status de um pedido
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Pedido pedido = pedidoService.updateStatus(id, status);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Somente usuários com a função ADMIN podem excluir pedidos
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        try {
            pedidoService.deleteById(id);
            // 204 indica que a exclusão foi realizada com sucesso
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {

            // Retorna 400 caso ocorra algum erro durante a exclusão
            return ResponseEntity.badRequest().build();
        }
    }
}
