package searchapp.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.dao.PageDAO;
import searchapp.entity.Page;
import searchapp.config.HibernateSessionFactoryUtil;

import java.util.List;

public class PageDAOimpl implements PageDAO {

    @Override
    public Page findById(int id){
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Page.class, id);
    }

    @Override
    public void save(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(page);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(page);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Page user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Page> findAll() {
        List<Page> users = (List<Page>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Page").list();
        return users;
    }
}
