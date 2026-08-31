package swagger.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")

// Define a ordem das propriedades retornadas pelo JSON
@JsonPropertyOrder({"codigo"})
public class Pedido {


    // Expõe o ID no JSON utilizando o nome codigo
	@JsonProperty("codigo")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Relaciona o pedido ao cliente que realizou a compra
    @JsonBackReference
    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Define o formato da data quandp o objeto for convertido para JSON
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Data do pedido é obrigatória")
    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    // O status é utilizado internamente pela aplicação
    // e não é exibido na resposta JSON
    @JsonIgnore
    @NotNull(message = "Status é obrigatório")
    @Column(nullable = false)
    private String status;


    // Um pedido pode possuir vários itens.
    // orphanRemoval permite remover itens que deixaram de pertencer ao pedido.
    @JsonBackReference
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    // Método para calcular total
    public BigDecimal getTotal() {
        // Se não há itens, retorna zero
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Inicia o total com zero
        BigDecimal total = BigDecimal.ZERO;
        
        // Percorre cada item do pedido
        for (ItemPedido item : itens) {
            // Para cada item, pega o subtotal (quantidade × preço unitário)
            BigDecimal subtotal = item.getSubtotal();
            // Soma ao total geral
            total = total.add(subtotal);
        }
        
        return total;
    }
    
    // Método utilitário para adicionar item
    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        itens.add(item);
    }
    
    // Método utilitário para remover item
    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
    }
}
