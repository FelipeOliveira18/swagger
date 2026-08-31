package swagger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import swagger.model.Cliente;

// Repository responsável pelo acesso aos dados de Cliente
// JpaRepository já fornece operações como salvar, buscar, atualizar e excluir
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Busca um cliente pelo e-mail
    Optional<Cliente> findByEmail(String email);

    // Verifica se já existe algum cliente utilizando o e-mail informado
    boolean existsByEmail(String email);

    // Pesquisa clientes cujo nome contenha o texto informado
    // ignorando diferenças entre letras maiúsculas e minúsculas
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Busca o cliente pelo ID carregando também seus pedidos
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.pedidos WHERE c.id = :id")
    Optional<Cliente> findByIdWithPedidos(@Param("id") Long id);
}
