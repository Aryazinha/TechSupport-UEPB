package br.uepb.techsupport.exception;

public class UsuarioNaoEncontradoException extends TechSupportException {

    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado ou senha incorreta.");
    }

    public UsuarioNaoEncontradoException(String identificador) {
        super("Não foi possível encontrar um usuário vinculado ao identificador: " + identificador);
    }
}