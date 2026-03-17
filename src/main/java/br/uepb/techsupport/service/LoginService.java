package br.uepb.techsupport.service;

import br.uepb.techsupport.exception.UsuarioNaoEncontradoException;
import br.uepb.techsupport.model.entidade.Pessoa;
import br.uepb.techsupport.repository.UsuarioRepository;
import br.uepb.techsupport.util.SenhaUtil;
import br.uepb.techsupport.util.SessaoUsuario;
import jakarta.persistence.NoResultException;

public class LoginService {

    private final UsuarioRepository repository = new UsuarioRepository();

    public boolean realizarLogin(String email, String senhaDigitada) {
        Pessoa usuarioEncontrado = null;

        try {
            usuarioEncontrado = repository.buscarTecnicoPorEmail(email);
        } catch (NoResultException e) {
            try {
                usuarioEncontrado = repository.buscarClientePorEmail(email);
            } catch (NoResultException e2) {
                throw new UsuarioNaoEncontradoException(email);
            }
        }
        if (SenhaUtil.verificarSenha(senhaDigitada, usuarioEncontrado.getSenha())) {
            SessaoUsuario.setUsuarioLogado(usuarioEncontrado);
            return true;
        }

        return false; // Senha incorreta
    }
}