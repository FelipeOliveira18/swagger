package swagger.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

	// Identificador unico do item
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	// Cada item pertence a um pedido
	// LAZY evita carregar o pedido automaticamente quando o item é consultado
	@JsonBackReference
	@NotNull(message = "Pedido é obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pedido_id", nullable = false)
	private Pedido pedido;

	// Cada item tambem esta relacionado a um produto
	@JsonBackReference
	@NotNull(message = "Produto é obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	// Define a quantidade comprada
	// A quantidade precisa ser informada e deve ser pelo menos 1
	@NotNull(message = "Quantidade é obrigatória")
	@Min(value = 1, message = "Quantidade deve ser pelo menos 1")
	@Column(nullable = false)
	private Integer quantidade;

	// Armazena o preço do produto no momento da venda
	@NotNull(message = "Preço unitário é obrigatório")
	@Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precoUnitario;
	
    // Método para calcular subtotal
    public BigDecimal getSubtotal() {
        if (precoUnitario == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
		// Calcula preço unitario * quantidade
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

}
