package br.uepb.techsupport.service;

import br.uepb.techsupport.model.entidade.Tecnico;

public class TecnicoService extends UsuarioService<Tecnico> {

    public void cadastrarTecnico(Tecnico novo) {
        super.validarEHash(novo);

        novo.setDisponivel(true);
        novo.setQuantidadeOsResolvidas(0);

        repository.salvar(novo);
    }
}