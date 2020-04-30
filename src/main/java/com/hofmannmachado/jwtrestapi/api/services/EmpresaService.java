package com.hofmannmachado.jwtrestapi.api.services;

import com.hofmannmachado.jwtrestapi.api.entities.Empresa;

import java.util.Optional;

public interface EmpresaService {

    /**
     * Retorna uma empresa dado um CNPJ
     *
     * @param cnpj
     * @return Optional<Empresa>
     */
    Optional<Empresa> buscarPorCnpj(String cnpj);

    /** Cadastra uma nova empresa na base dedados.
     *
     * @param empresa
     * @return Empresa
     */
    Empresa persistir(Empresa empresa);
}
