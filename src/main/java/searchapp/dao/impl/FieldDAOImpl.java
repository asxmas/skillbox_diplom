package searchapp.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.config.HibernateSessionFactoryUtil;
import searchapp.entity.Field;

import java.util.List;

public class FieldDAOImpl implements searchapp.dao.FieldDAO {

    @Override
    public Field findFieldById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Field.class, id);
    }

    @Override
    public void saveField(Field field) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(field);
        tx1.commit();
        session.close();
    }

    @Override
    public void updateField(Field field) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(field);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteField(Field field) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(field);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Field> findAllFields() {
        List<Field> fields = (List<Field>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Field ").list();
        return fields;
    }
}
