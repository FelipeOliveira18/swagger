package swagger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swagger.model.Produto;
import swagger.repository.ProdutoRepository;

// Camada responsável pelas regras de negócio dos produtos
@Service
public class ProdutoService {

    // Repository responsável pelo acesso aos produtos
    @Autowired
    private ProdutoRepository produtoRepository;

    // Retorna todos os produtos
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    // Busca um produto pelo ID
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    // Pesquisa produtos pelo nome
    public List<Produto> findByNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Busca produtos dentro de uma faixa de preço
    public List<Produto> findByPrecoRange(BigDecimal min, BigDecimal max) {
        return produtoRepository.findByPrecoBetween(min, max);
    }

    // Retorna somente produtos que possuem estoque
    public List<Produto> findComEstoque() {
        return produtoRepository.findByEstoqueGreaterThan(0);
    }

    // Retorna produtos sem estoque
    public List<Produto> findSemEstoque() {
        return produtoRepository.findProdutosSemEstoque();
    }

    // Pesquisa um termo no nome ou descrição
    public List<Produto> search(String termo) {
        return produtoRepository.searchByNomeOrDescricao(termo);
    }

    // Salva um novo produto
    @Transactional
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Atualiza os dados de um produto
    @Transactional
    public Produto update(Long id, Produto produtoDetails) {

        // Busca o produto que será atualizado
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        
        produto.setNome(produtoDetails.getNome());
        produto.setDescricao(produtoDetails.getDescricao());
        produto.setPreco(produtoDetails.getPreco());
        produto.setEstoque(produtoDetails.getEstoque());
        
        return produtoRepository.save(produto);
    }

    // Exclui um produto somente quando ele não possui pedidos associados
    @Transactional
    public void deleteById(Long id) {
        // Busca o produto antes da exclusão
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        
        // Verifica se o produto está em algum pedido antes de deletar
        if (!produto.getItensPedido().isEmpty()) {
            throw new RuntimeException("Não é possível excluir produto com pedidos associados");
        }
        
        produtoRepository.deleteById(id);
    }

    // Adiciona ou remove determinada quantidade do estoque
    @Transactional
    public Produto atualizarEstoque(Long id, Integer quantidade) {

        // Busca o produto que terá seu estoque alterado
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));

        // Calcula o novo estoque após a movimentação
        int novoEstoque = produto.getEstoque() + quantidade;

        // Impede que o estoque fique negativo
        if (novoEstoque < 0) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " + produto.getEstoque());
        }
        
        produto.setEstoque(novoEstoque);
        return produtoRepository.save(produto);
    }
}