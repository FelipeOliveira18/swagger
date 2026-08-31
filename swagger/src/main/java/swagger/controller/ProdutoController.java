package swagger.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import swagger.model.Produto;
import swagger.service.ProdutoService;

// Controller responsável pelas operações relacionadas aos produtos
@RestController
// Define o caminho base dos endpoints de produtos
@RequestMapping("/api/produtos")
// Permite requisições vindas de diferentes origens
@CrossOrigin(origins = "*")
public class ProdutoController {

    // Serviço responsável pelas regras de negócio dos produtos
    @Autowired
    private ProdutoService produtoService;

    // Permite que usuários ADMIN ou USER consultem os produtos
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoService.findAll();
    }

    // Busca um produto pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(id);

        // Retorna o produto ou 404 caso ele não seja encontrado.
        return produto.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Pesquisa produtos pelo nome
    @GetMapping("/search")
    public List<Produto> searchProdutos(@RequestParam String nome) {
        return produtoService.findByNome(nome);
    }

    // Pesquisa produtos utilizando um termo que pode estar
    // no nome ou na descrição
    @GetMapping("/search-termo")
    public List<Produto> searchProdutosByTermo(@RequestParam String termo) {
        return produtoService.search(termo);
    }

    // Retorna produtos cujo preço esteja dentro do intervalo informado
    @GetMapping("/preco-range")
    public List<Produto> getProdutosByPrecoRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return produtoService.findByPrecoRange(min, max);
    }

    // Retorna somente produtos que possuem estoque disponível
    @GetMapping("/com-estoque")
    public List<Produto> getProdutosComEstoque() {
        return produtoService.findComEstoque();
    }

    // Retorna os produtos que estão sem estoque
    @GetMapping("/sem-estoque")
    public List<Produto> getProdutosSemEstoque() {
        return produtoService.findSemEstoque();
    }

    // Cadastra um novo produto
    @PostMapping
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody Produto produto) {
        try {
            Produto savedProduto = produtoService.save(produto);
            return ResponseEntity.ok(savedProduto);
        } catch (Exception e) {
            // Retorna 400 quando ocorre algum erro no cadastro
            return ResponseEntity.badRequest().build();
        }
    }

    // Atualiza os dados de um produto existente
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @Valid @RequestBody Produto produtoDetails) {
        try {
            Produto updatedProduto = produtoService.update(id, produtoDetails);
            return ResponseEntity.ok(updatedProduto);
        } catch (RuntimeException e) {
            // Retorna 404 quando o produto não é encontrado
            return ResponseEntity.notFound().build();
        }
    }

    // Exclui um produto pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        try {
            produtoService.deleteById(id);

            // Retorna 204 após a exclusão
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Retorna 400 caso a exclusão não possa ser realizada
            return ResponseEntity.badRequest().build();
        }
    }

    // Permite adicionar ou remover uma quantidade do estoque
    @PatchMapping("/{id}/estoque")
    public ResponseEntity<Produto> atualizarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        try {
            Produto produto = produtoService.atualizarEstoque(id, quantidade);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            // Retorna 400 quando ocorre algum problema com o estoque
            return ResponseEntity.badRequest().build();
        }
    }
}
                    