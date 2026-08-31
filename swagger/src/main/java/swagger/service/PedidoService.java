package swagger.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swagger.model.Cliente;
import swagger.model.ItemPedido;
import swagger.model.Pedido;
import swagger.model.Produto;
import swagger.repository.ClienteRepository;
import swagger.repository.ItemPedidoRepository;
import swagger.repository.PedidoRepository;
import swagger.repository.ProdutoRepository;

// Camada responsável pelas regras de negócio relacionadas aos pedidos
@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    // Repository utilizado para validar e buscar o cliente do pedido
    @Autowired
    private ClienteRepository clienteRepository;

    // Repository utilizado para consultar os produtos
    @Autowired
    private ProdutoRepository produtoRepository;

    // Repository utilizado para salvar os itens dos pedidos
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    // Serviço utilizado para realizar alterações no estoque
    @Autowired
    private ProdutoService produtoService;

    // Retorna todos os pedidos
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    // Busca um pedido pelo ID junto com seus itens
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findByIdWithItens(id);
    }

    // Busca os pedidos pertencentes a determinado cliente
    public List<Pedido> findByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteIdWithItens(clienteId);
    }

    // Busca pedidos pelo status
    public List<Pedido> findByStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }

    // Busca pedidos realizados entre duas datas
    public List<Pedido> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    // Cadastra um pedido e seus respectivos itens
    @Transactional
    public Pedido save(Pedido pedido) {
        // Verifica se o cliente existe
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + pedido.getCliente().getId()));
        
        pedido.setCliente(cliente);
        // Define automaticamente a data atual para o pedido
        pedido.setDataPedido(LocalDate.now());
        
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        // Salva os itens do pedido
        for (ItemPedido item : pedido.getItens()) {
            item.setPedido(pedidoSalvo);
            
            // Verifica se o produto existe e tem estoque
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + item.getProduto().getId()));

            // Verifica se existe estoque suficiente para atender a quantidade solicitada
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome() + 
                                          ". Disponível: " + produto.getEstoque());
            }
            
            item.setProduto(produto);

            // Salva no item o preço atual do produto
            item.setPrecoUnitario(produto.getPreco());
            
            // Atualiza o estoque
            produtoService.atualizarEstoque(produto.getId(), -item.getQuantidade());
            
            itemPedidoRepository.save(item);
        }

        // Busca novamente o pedido já com todos os seus itens
        return pedidoRepository.findByIdWithItens(pedidoSalvo.getId())
            .orElseThrow(() -> new RuntimeException("Erro ao buscar pedido salvo"));
    }


    // Atualiza somente o status de um pedido
    @Transactional
    public Pedido updateStatus(Long id, String novoStatus) {
        // Localiza o pedido antes de realizar a alteração
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    // Exclui um pedido e, quando necessário, devolve seus produtos ao estoque
    @Transactional
    public void deleteById(Long id) {

        // Busca o pedido juntamente com seus itens
        Pedido pedido = pedidoRepository.findByIdWithItens(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        
        // Devolve os produtos ao estoque se o pedido for cancelado/excluído
        if (!"CANCELADO".equals(pedido.getStatus())) {
            // Percorre os itens do pedido para devolver suas quantidades
            for (ItemPedido item : pedido.getItens()) {
                produtoService.atualizarEstoque(item.getProduto().getId(), item.getQuantidade());
            }
        }
        
        pedidoRepository.deleteById(id);
    }

    // Calcula o valor total de um pedido
    public BigDecimal calcularTotalPedido(Long pedidoId) {
        // Busca o pedido junto com seus itens
        Pedido pedido = pedidoRepository.findByIdWithItens(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + pedidoId));

        // Utiliza a regra de cálculo definida na entidade Pedido
        return pedido.getTotal();
    }
}