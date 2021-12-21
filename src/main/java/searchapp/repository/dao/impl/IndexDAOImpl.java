package searchapp.repository.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.config.HibernateSessionFactoryUtil;
import searchapp.entity.Index;
import searchapp.entity.Lemma;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class IndexDAOImpl implements searchapp.repository.dao.IndexDAO {


    @Override
    public Index findIndexById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Index.class, id);
    }

    @Override
    public void saveIndex(Index index) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(index);
        tx1.commit();
        session.close();
    }

    @Override
    public void saveIndexes(List<Index> indexList){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        indexList.forEach(session::save);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Index> findIndexesByPageIds(List<Integer> indexIdList){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Index> cr = cb.createQuery(Index.class);
        Root<Index> root = cr.from(Index.class);
        CriteriaBuilder.In<Integer> in = cb.in(root.get("page"));
        indexIdList.forEach(in::value);
        List<Index> indexes = session.createQuery(cr.select(root).where(in)).getResultList();
        session.close();
        return indexes;
    }

    @Override
    public void updateIndex(Index index) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(index);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteIndex(Index index) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(index);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Index> findAllIndexes() {
        List<Index> indexes = (List<Index>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Index ").list();
        return indexes;
    }
}
