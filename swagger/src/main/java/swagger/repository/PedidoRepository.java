package swagger.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.Pedido;

// Repository responsável pelo acesso aos dados dos pedidos
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Busca pedidos pertencentes a um cliente específico
    List<Pedido> findByClienteId(Long clienteId);
    // Busca pedidos de acordo com o status
    List<Pedido> findByStatus(String status);
    // Busca pedidos realizados entre duas datas
    List<Pedido> findByDataPedidoBetween(LocalDate inicio, LocalDate fim);

    // Busca um pedido pelo ID carregando também seus itens
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);

    // Busca os pedidos de um cliente já carregando seus itens
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.cliente.id = :clienteId")
    List<Pedido> findByClienteIdWithItens(@Param("clienteId") Long clienteId);

    // Conta quantos pedidos possuem determinado status
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = :status")
    Long countByStatus(@Param("status") String status);
}

