package br.uepb.techsupport.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("techsupport-uepb");

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}