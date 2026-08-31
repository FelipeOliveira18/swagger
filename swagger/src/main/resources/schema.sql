CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE
);

CREATE TABLE produtos (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome TEXT NOT NULL,
    descricao TEXT,
    preco REAL NOT NULL,
    estoque INTEGER NOT NULL
);

CREATE TABLE pedidos (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT,
    data_pedido DATE NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (cliente_id) R REFERENCES clientes(id)
);

CREATE TABLE itens_pedido (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT,
    produto_id BIGINT,
    quantidade INTEGER NOT NULL,
    preco_unitario REAL NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);