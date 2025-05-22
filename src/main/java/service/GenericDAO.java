package service;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDAO<T> {

    private final Class<T> type;
    protected SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public GenericDAO(Class<T> type) {
        this.type = type;
    }

    public void save(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        }
    }

    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        }
    }

    public void delete(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        }
    }

    public T findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(type, id);
        }
    }

    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM " + type.getSimpleName();
            return session.createQuery(hql, type).getResultList();
        }
    }
}
