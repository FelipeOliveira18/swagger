-- Inserir clientes
INSERT INTO clientes (nome, email, telefone, data_nascimento) VALUES
('Maria Oliveira', 'maria.oliveira@gmail.com', '(11) 98877-1234', '1987-05-14'),
('João Santos', 'joao.santos@hotmail.com', '(21) 97766-4521', '1990-03-22'),
('Ana Pereira', 'ana.pereira@yahoo.com', '(31) 98455-7890', '1985-10-10'),
('Carlos Lima', 'carlos.lima@gmail.com', '(41) 99211-3344', '1993-12-02'),
('Fernanda Souza', 'fernanda.souza@gmail.com', '(51) 98123-8877', '1991-06-09'),
('Rafael Martins', 'rafael.martins@gmail.com', '(61) 98345-9988', '1988-07-21'),
('Juliana Rocha', 'juliana.rocha@outlook.com', '(71) 98765-5544', '1995-09-25'),
('Eduardo Silva', 'eduardo.silva@gmail.com', '(81) 98654-3322', '1989-04-30'),
('Patrícia Almeida', 'patricia.almeida@gmail.com', '(85) 98122-4466', '1992-02-19'),
('Lucas Fernandes', 'lucas.fernandes@gmail.com', '(19) 98444-6677', '1996-08-13');

-- Inserir produtos
INSERT INTO produtos (nome, descricao, preco, estoque) VALUES
('Notebook Dell Inspiron 15', 'Notebook 15,6" Intel i7, 16GB RAM, SSD 512GB', 4899.90, 15),
('Smartphone Samsung ', 'Tela 6,1" AMOLED, 256GB, 8GB RAM', 3899.00, 30),
('Smart TV LG 55" 4K', Galaxy S23'Painel LED UHD 4K, HDR, Wi-Fi, webOS', 2999.00, 20),
('Fone Bluetooth JBL Tune 510BT', 'Som estéreo, até 40h de bateria', 349.90, 50),
('Mouse Logitech MX Master 3', 'Mouse sem fio ergonômico, USB-C, até 70 dias de bateria', 599.00, 25),
('Teclado Mecânico Redragon Kumara', 'Teclado gamer RGB switches Outemu Blue', 289.00, 40),
('Monitor Samsung 27" Curvo', 'Tela curva Full HD, 75Hz, HDMI', 1199.00, 18),
('Cadeira Gamer ThunderX3 TGC12', 'Cadeira ergonômica com ajuste de altura e encosto reclinável', 1399.00, 12),
('Headset HyperX Cloud II', 'Headset gamer 7.1 surround, microfone removível', 799.00, 30),
('Impressora HP DeskJet 2774', 'Multifuncional com Wi-Fi e impressão colorida', 499.00, 22);

-- Inserir pedidos
INSERT INTO pedidos (cliente_id, data_pedido, status) VALUES
(1, '2025-10-01', 'Enviado'),
(2, '2025-10-03', 'Entregue'),
(3, '2025-10-05', 'Entregue'),
(4, '2025-10-08', 'Em processamento'),
(5, '2025-10-10', 'Entregue'),
(6, '2025-10-11', 'Cancelado'),
(7, '2025-10-13', 'Enviado'),
(8, '2025-10-15', 'Entregue'),
(9, '2025-10-18', 'Em processamento'),
(10, '2025-10-20', 'Entregue');

-- Inserir itens dos pedidos (cada pedido tem pelo menos 5 produtos)
-- Pedido 1
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(1, 1, 1, 4899.90),
(1, 4, 2, 349.90),
(1, 6, 1, 289.00),
(1, 5, 1, 599.00),
(1, 9, 1, 799.00);

-- Pedido 2
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(2, 2, 1, 3899.00),
(2, 7, 1, 1199.00),
(2, 4, 1, 349.90),
(2, 10, 1, 499.00),
(2, 5, 1, 599.00);

-- Pedido 3
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(3, 3, 1, 2999.00),
(3, 9, 1, 799.00),
(3, 4, 1, 349.90),
(3, 6, 1, 289.00),
(3, 8, 1, 1399.00);

-- Pedido 4
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(4, 1, 1, 4899.90),
(4, 7, 1, 1199.00),
(4, 5, 1, 599.00),
(4, 4, 1, 349.90),
(4, 9, 1, 799.00);

-- Pedido 5
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(5, 2, 1, 3899.00),
(5, 3, 1, 2999.00),
(5, 10, 1, 499.00),
(5, 4, 2, 349.90),
(5, 8, 1, 1399.00);

-- Pedido 6
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(6, 1, 1, 4899.90),
(6, 2, 1, 3899.00),
(6, 4, 1, 349.90),
(6, 9, 1, 799.00),
(6, 5, 1, 599.00);

-- Pedido 7
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(7, 6, 1, 289.00),
(7, 10, 1, 499.00),
(7, 5, 1, 599.00),
(7, 9, 1, 799.00),
(7, 8, 1, 1399.00);

-- Pedido 8
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(8, 3, 1, 2999.00),
(8, 7, 1, 1199.00),
(8, 9, 1, 799.00),
(8, 5, 1, 599.00),
(8, 6, 1, 289.00);

-- Pedido 9
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(9, 4, 2, 349.90),
(9, 5, 1, 599.00),
(9, 6, 1, 289.00),
(9, 8, 1, 1399.00),
(9, 9, 1, 799.00);

-- Pedido 10
INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES
(10, 1, 1, 4899.90),
(10, 2, 1, 3899.00),
(10, 3, 1, 2999.00),
(10, 4, 2, 349.90),
(10, 10, 1, 499.00);
