package searchapp.repository.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.config.HibernateSessionFactoryUtil;
import searchapp.entity.Index;

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
    public List<Index> findIndexesByLemmaId(Integer lemmaId){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Index> cr = cb.createQuery(Index.class);
        Root<Index> root = cr.from(Index.class);
        CriteriaBuilder.In<Integer> in = cb.in(root.get("lemma"));
        in.value(lemmaId);
        List<Index> indexes = session.createQuery(cr.select(root).where(in)).getResultList();
        session.close();
        return indexes;
    }

    @Override
    public List<Index> findIndexesByPageIds(List<Integer> pageIds){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Index> query = cb.createQuery(Index.class);
        Root<Index> root = query.from(Index.class);
        List<Index> indexList = session.createQuery(query.select(root)
                        .where(root.get("page").in(pageIds)))
                .getResultList();
        session.close();
        return indexList;
    }

    @Override
    public List<Index> getIndexListForRelevance(List<Integer> pageIdList, List<Integer> lemmIdList){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Index> query = cb.createQuery(Index.class);
        Root<Index> root = query.from(Index.class);
        List<Index> indexList = session.createQuery(query.select(root)
                .where(cb.and(root.get("page").in(pageIdList),root.get("lemma").in(lemmIdList))))
                .getResultList();
        session.close();
        return indexList;
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
