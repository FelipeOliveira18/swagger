package swagger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.ItemPedido;

// Repository responsável pelo acesso aos itens dos pedidos
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    // Busca todos os itens pertencentes a um determinado pedido
    List<ItemPedido> findByPedidoId(Long pedidoId);
    // Busca todos os itens relacionados a um determinado produto
    List<ItemPedido> findByProdutoId(Long produtoId);

    // Busca um item específico relacionando pedido e produto
    @Query("SELECT ip FROM ItemPedido ip WHERE ip.pedido.id = :pedidoId AND ip.produto.id = :produtoId")
    Optional<ItemPedido> findByPedidoAndProduto(@Param("pedidoId") Long pedidoId, @Param("produtoId") Long produtoId);

    // Soma a quantidade total vendida de determinado produto
    @Query("SELECT SUM(ip.quantidade) FROM ItemPedido ip WHERE ip.produto.id = :produtoId")
    Integer findTotalVendidoByProdutoId(@Param("produtoId") Long produtoId);
}
