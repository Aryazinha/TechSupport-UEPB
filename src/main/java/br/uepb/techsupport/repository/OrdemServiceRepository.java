package br.uepb.techsupport.repository;

import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.StatusOS;
import br.uepb.techsupport.model.system.OrdemServico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdemServiceRepository {

    private static final String COUNT_BY_STATUS = "SELECT COUNT(os) FROM OrdemServico os WHERE os.status = :status";
    private static final String COUNT_TOTAL_ORDER = "SELECT COUNT(os) FROM OrdemServico os";
    private static final String FIND_ALL_ORDER = "SELECT os FROM OrdemServico os";
    private static final String FIND_BY_TECNICO = "SELECT os FROM OrdemServico os WHERE os.tecnico = :tecnico";
    private static final String FIND_BY_CLIENTE = "SELECT os FROM OrdemServico os WHERE os.cliente = :cliente ORDER BY os.dataAbertura DESC";
    private static final String COUNT_TECNICOS_DISPONIVEIS = "SELECT COUNT(t) FROM Tecnico t WHERE t.disponivel = true";

    public void salvar(OrdemServico os) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (os.getId() == null) {
                em.persist(os);
            } else {
                em.merge(os);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Erro ao persistir Ordem de Serviço: " + e.getMessage());
        } finally {
            fecharEntityManager(em);
        }
    }

    public List<OrdemServico> buscarTodas() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(FIND_ALL_ORDER, OrdemServico.class).getResultList();
        } finally {
            fecharEntityManager(em);
        }
    }

    public List<OrdemServico> buscarPorTecnico(Tecnico tecnico) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(FIND_BY_TECNICO, OrdemServico.class)
                    .setParameter("tecnico", tecnico)
                    .getResultList();
        } finally {
            fecharEntityManager(em);
        }
    }

    public List<OrdemServico> buscarPorCliente(Cliente cliente) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(FIND_BY_CLIENTE, OrdemServico.class)
                    .setParameter("cliente", cliente)
                    .getResultList();
        } finally {
            fecharEntityManager(em);
        }
    }

    public void atualizar(OrdemServico os) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(os);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Map<String, Long> buscarEstatisticas() {
        EntityManager em = HibernateUtil.getEntityManager();
        Map<String, Long> stats = new HashMap<>();
        try {
            stats.put("abertas", em.createQuery(COUNT_BY_STATUS, Long.class).setParameter("status", StatusOS.ABERTA).getSingleResult());
            stats.put("concluidas", em.createQuery(COUNT_BY_STATUS, Long.class).setParameter("status", StatusOS.CONCLUIDA).getSingleResult());
            stats.put("total", em.createQuery(COUNT_TOTAL_ORDER, Long.class).getSingleResult());
            stats.put("tecnicos", em.createQuery(COUNT_TECNICOS_DISPONIVEIS, Long.class).getSingleResult());
            return stats;
        } finally {
            fecharEntityManager(em);
        }
    }

    public void atualizarTecnico(Tecnico tecnico) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tecnico);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Falha ao atualizar dados do técnico: " + e.getMessage());
        } finally {
            fecharEntityManager(em);
        }
    }

    private void fecharEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}