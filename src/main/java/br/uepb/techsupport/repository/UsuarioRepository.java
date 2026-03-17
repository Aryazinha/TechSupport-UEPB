package br.uepb.techsupport.repository;

import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.entidade.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class UsuarioRepository {

    private static final String FIND_TECNICO_BY_EMAIL = "FROM Tecnico t WHERE t.email = :email";
    private static final String FIND_CLIENTE_BY_EMAIL = "FROM Cliente c WHERE c.email = :email";
    private static final String COUNT_TECNICO_BY_CPF = "SELECT COUNT(t) FROM Tecnico t WHERE t.cpf = :cpf";
    private static final String COUNT_TECNICO_BY_EMAIL = "SELECT COUNT(t) FROM Tecnico t WHERE t.email = :email";
    private static final String COUNT_CLIENTE_BY_TELEFONE = "SELECT COUNT(t) FROM Cliente t WHERE t.telefone = :telefone";

    public Tecnico buscarTecnicoPorEmail(String email) throws NoResultException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(FIND_TECNICO_BY_EMAIL, Tecnico.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } finally {
            fecharEntityManager(em);
        }
    }

    public Cliente buscarClientePorEmail(String email) throws NoResultException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(FIND_CLIENTE_BY_EMAIL, Cliente.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } finally {
            fecharEntityManager(em);
        }
    }

    public boolean existeCpf(String cpf) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Long contagem = em.createQuery(COUNT_TECNICO_BY_CPF, Long.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
            return contagem > 0;
        } finally {
            fecharEntityManager(em);
        }
    }

    public boolean existeEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Long contagem = em.createQuery(COUNT_TECNICO_BY_EMAIL, Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return contagem > 0;
        } finally {
            fecharEntityManager(em);
        }
    }

    public boolean jaExisteTelefone(String telefone) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            Long contagem = em.createQuery(COUNT_CLIENTE_BY_TELEFONE, Long.class)
                    .setParameter("telefone", telefone)
                    .getSingleResult();
            return contagem > 0;
        } finally {
            fecharEntityManager(em);
        }
    }

    public void salvar(Object entidade) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
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