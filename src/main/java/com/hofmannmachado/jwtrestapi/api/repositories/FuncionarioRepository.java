package com.hofmannmachado.jwtrestapi.api.repositories;

import com.hofmannmachado.jwtrestapi.api.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Transactional(readOnly = true)
    Funcionario findByCpf(String cpf);

    @Transactional(readOnly = true)
    Funcionario findByEmail(String email);

    @Transactional(readOnly = true)
    Funcionario findByCpfOrEmail(String cpf, String email);
}
