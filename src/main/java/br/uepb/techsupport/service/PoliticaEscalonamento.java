package br.uepb.techsupport.service;

import br.uepb.techsupport.model.system.OrdemServico;

// Interface que calcula o peso antigravidade
@FunctionalInterface
public interface PoliticaEscalonamento {

    double CalcularPeso(OrdemServico os);
}
