package br.uepb.techsupport.util;

import br.uepb.techsupport.model.entidade.Pessoa;

public class SessaoUsuario {
    private static Pessoa usuarioLogado;

    public static void setUsuarioLogado(Pessoa usuario) { usuarioLogado = usuario; }
    public static Pessoa getUsuarioLogado() { return usuarioLogado; }

    public static void encerrarSessao() { usuarioLogado = null; }
}
