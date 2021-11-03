package searchapp.repository.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.repository.dao.PageDAO;
import searchapp.entity.Page;
import searchapp.config.HibernateSessionFactoryUtil;

import java.util.List;

public class PageDAOimpl implements PageDAO {

    @Override
    public Page findPageById(int id){
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Page.class, id);
    }

    @Override
    public void savePage(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(page);
        tx1.commit();
        session.close();
    }

    @Override
    public void updatePage(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(page);
        tx1.commit();
        session.close();
    }

    @Override
    public void deletePage(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(page);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Page> findAllPages() {
        List<Page> pages = (List<Page>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Page").list();
        return pages;
    }
}
