package service;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDAO<T> {

    private final Class<T> type;
    protected final Session session;

    public GenericDAO(Class<T> type, Session session) {
        this.type = type;
        this.session = session;
    }

    public void save(T entity) {
        Transaction tr = session.beginTransaction();
        session.save(entity);
        tr.commit();
    }

    public void update(T entity) {
        Transaction tr = session.beginTransaction();
        session.update(entity);
        tr.commit();
    }

    public void delete(T entity) {
        Transaction tr = session.beginTransaction();
        session.delete(entity);
        tr.commit();
    }

    public T findById(int id) {
        return session.get(type, id);
    }

    public List<T> findAll() {
        String hql = "FROM " + type.getSimpleName();
        return session.createQuery(hql, type).getResultList();
    }

    public List<T> getWithPagination(int page, int pageSize) {
        String hql = "FROM Citizen";
        return session.createQuery(hql, type)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
