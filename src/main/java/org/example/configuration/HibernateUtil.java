package org.example.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            //TODO:build new sessionFactory
            //sessionFactory = new Configuration().configure().buildSessionFactory();
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();

        }
        return sessionFactory;

    }
}