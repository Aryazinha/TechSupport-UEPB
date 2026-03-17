package br.uepb.techsupport.service;

import br.uepb.techsupport.exception.FormatoInvalidoException;
import br.uepb.techsupport.exception.TelefoneJaCadastradoException;
import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.util.ValidadorDados;

public class ClienteService extends UsuarioService<Cliente> {

    public void cadastrarCliente(Cliente novo) {
        super.validarEHash(novo);

        if (!ValidadorDados.isTelefoneValido(novo.getTelefone())) {
            throw new FormatoInvalidoException("Telefone inválido.");
        }

        if(repository.jaExisteTelefone(novo.getTelefone())) {
            throw new TelefoneJaCadastradoException(novo.getTelefone());
        }

        repository.salvar(novo);
    }
}
