package swagger.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa um cliente armazenado no banco de dados
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    // Identificador unico do cliente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O nome é obrigatoriamente tanto na validação da API quanto na estutua do banco
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    /*
    Valida se o valor possui formato de e-mail e se fo informado,
    o campo tambem possui um restrição de unidade no banco
     */
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;
    
    private String telefone;

    // Define o nome da coluna correpondente no banco de dados
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;


    // Um cliente pode possuir varios pedidos,
    // JsonManagedReference ajuda a controlar a serialização do relacionamento
    // Para evitar referencias circulares no JSON
    @JsonManagedReference
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();

}