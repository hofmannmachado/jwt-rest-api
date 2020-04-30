package com.hofmannmachado.jwtrestapi.api.controllers;

import com.hofmannmachado.jwtrestapi.api.dtos.CadastroPFDto;
import com.hofmannmachado.jwtrestapi.api.entities.Empresa;
import com.hofmannmachado.jwtrestapi.api.entities.Funcionario;
import com.hofmannmachado.jwtrestapi.api.enums.PerfilEnum;
import com.hofmannmachado.jwtrestapi.api.response.Response;
import com.hofmannmachado.jwtrestapi.api.services.EmpresaService;
import com.hofmannmachado.jwtrestapi.api.services.FuncionarioService;
import com.hofmannmachado.jwtrestapi.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("${app.api.cadastrar-pf.url}")
@CrossOrigin(origins = {"${settings.cors_origin}"})
public class CadastroPFController {

    private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FuncionarioService funcionarioService;

    public CadastroPFController() {
    }

    /**
     * Cadastra uma pessoa física no sistema
     *
     * @param cadastroPFDto
     * @param result
     * @return ResponseEntity<Response < CadastroPFDto>>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(
            @Valid @RequestBody CadastroPFDto cadastroPFDto,
            BindingResult result) throws NoSuchAlgorithmException {
        log.info("Cadastrando PF: {}", cadastroPFDto.toString());
        Response<CadastroPFDto> response = new Response<CadastroPFDto>();

        validarDadosExistentes(cadastroPFDto, result);
        Funcionario funcionario = converterDtoParaFuncionario(cadastroPFDto, result);

        if (result.hasErrors()) {
            log.error("Erro validando dados de cadastro de PF: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
        this.funcionarioService.persistir(funcionario);

        response.setData(converterCadastroPFDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {
        CadastroPFDto cadastroPFDto = new CadastroPFDto();
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setNome(funcionario.getNome());

        funcionario.getQtdHorasAlmocoOpt().ifPresent(
                qtdHorasAlmoco -> cadastroPFDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco)))
        );
        funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
                qtdHorasTrabalho -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalho)))
        );
        funcionario.getValorHoraOpt().ifPresent(
                valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString()))
        );

        return cadastroPFDto;
    }

    private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPFDto.getNome());
        funcionario.setEmail(cadastroPFDto.getEmail());
        funcionario.setCpf(cadastroPFDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));

        cadastroPFDto.getQtdHorasAlmoco()
                .ifPresent(qtdHoras -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHoras)));
        cadastroPFDto.getQtdHorasTrabalhoDia()
                .ifPresent(qtd -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtd)));
        cadastroPFDto.getValorHora()
                .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
        return funcionario;
    }

    private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        if (!empresa.isPresent()) {
            result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
        }

        funcionarioService.buscarPorCpf(cadastroPFDto.getCpf()).ifPresent(
                func -> result.addError(new ObjectError("funcionario", "CPF existente"))
        );

        funcionarioService.buscarPorEmail(cadastroPFDto.getEmail()).ifPresent(
                func -> result.addError(new ObjectError("funcionario", "Email existente"))
        );
    }
}
