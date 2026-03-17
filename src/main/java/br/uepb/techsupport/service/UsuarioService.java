package br.uepb.techsupport.service;

import br.uepb.techsupport.exception.*;
import br.uepb.techsupport.model.entidade.Pessoa;
import br.uepb.techsupport.repository.UsuarioRepository;
import br.uepb.techsupport.util.SenhaUtil;
import br.uepb.techsupport.util.ValidadorDados;

public abstract class UsuarioService<T extends Pessoa> {

    protected final UsuarioRepository repository = new UsuarioRepository();

    public void validarEHash(T usuario) {
        if (!ValidadorDados.isCpfValido(usuario.getCpf())) {
            throw new FormatoInvalidoException("CPF inválido.");
        }
        if (!ValidadorDados.isEmailValido(usuario.getEmail())) {
            throw new FormatoInvalidoException("E-mail inválido.");
        }

        if (repository.existeCpf(usuario.getCpf())) {
            throw new CPFJaCadastradoException(usuario.getCpf());
        }
        if (repository.existeEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException(usuario.getEmail());
        }

        usuario.setSenha(SenhaUtil.hashSenha(usuario.getSenha()));
    }
}