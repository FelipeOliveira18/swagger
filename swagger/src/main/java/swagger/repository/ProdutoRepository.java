package swagger.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.Produto;
// Repository responsável pelo acesso aos dados dos produtos
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Pesquisa produtos pelo nome ignorando maiúsculas e minúsculas
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    // Busca produtos dentro de uma faixa de preço
    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);
    // Busca produtos que possuem estoque maior que o valor informado
    List<Produto> findByEstoqueGreaterThan(Integer estoque);

    // Busca produtos que estão sem estoque
    @Query("SELECT p FROM Produto p WHERE p.estoque = 0")
    List<Produto> findProdutosSemEstoque();

    // Pesquisa um termo tanto no nome quanto na descrição do produto
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Produto> searchByNomeOrDescricao(@Param("termo") String termo);
}


