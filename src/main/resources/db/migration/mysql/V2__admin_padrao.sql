INSERT INTO `empresa` (`id`, `cnpj`, `data_atualizacao`, `data_criacao`, `razao_social`) 
VALUES (NULL, '111111111111111', CURRENT_DATE(), CURRENT_DATE(), 'Empresa padrao');

INSERT INTO `funcionario` (`id`, `cpf`, `data_atualizacao`, `data_criacao`, `email`, `nome`, 
`perfil`, `qtd_horas_almoco`, `qtd_horas_trabalho_dia`, `senha`, `valor_hora`, `empresa_id`) 
VALUES (NULL, '11111111111', CURRENT_DATE(), CURRENT_DATE(), 'admin@admin.com', 'ADMIN', 'ROLE_ADMIN', NULL, NULL,
'$2a$10$VwyUIMBneQ6R0ft8HQyGk.03MLCLHjub/znHLCjW8ueB89xflT5XS', NULL,
(SELECT `id` FROM `empresa` WHERE `cnpj` = '111111111111111')); -- Senha 123123
