package searchapp.repository.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.repository.dao.PageDAO;
import searchapp.entity.Page;
import searchapp.config.HibernateSessionFactoryUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PageDAOimpl implements PageDAO {

    @Override
    public Page findPageById(int id){
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Page.class, id);
    }

    @Override
    public List<Page> findPagesByIds(List<Integer> pageIds){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Page> cr = cb.createQuery(Page.class);
        Root<Page> root = cr.from(Page.class);
        CriteriaBuilder.In<Integer> in = cb.in(root.get("id"));
        pageIds.forEach(in::value);
        List<Page> pages = session.createQuery(cr.select(root).where(in)).getResultList();
        session.close();
        return pages;
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
