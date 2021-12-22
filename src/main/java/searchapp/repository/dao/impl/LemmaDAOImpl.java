package searchapp.repository.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import searchapp.config.HibernateSessionFactoryUtil;
import searchapp.entity.Lemma;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public class LemmaDAOImpl implements searchapp.repository.dao.LemmaDAO {


    @Override
    public Lemma findLemmaById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Lemma.class, id);
    }

    @Override
    public Optional<Lemma> findLemmaByLemmaName(String lemmaName){

            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Lemma> cr = cb.createQuery(Lemma.class);
            Root<Lemma> root = cr.from(Lemma.class);
            cr.select(root);
            cr.where(cb.equal(root.get("lemmaName"), lemmaName));
            Query<Lemma> query = session.createQuery(cr);
            List<Lemma> results = query.getResultList();
            session.close();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Lemma> findLemmsByLemmaNames(List<String> lemmaNames){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Lemma> query = cb.createQuery(Lemma.class);
        Root<Lemma> root = query.from(Lemma.class);
        return session.createQuery(query.select(root)
                        .where(root.get("lemmaName")
                        .in(lemmaNames))
                        .orderBy(cb.asc(root.get("frequency"))))
                        .getResultList();
    }

    @Override
    public void saveLemma(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(lemma);
        tx1.commit();
        session.close();
    }

    @Override
    public void updateLemma(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(lemma);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteLemma(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(lemma);
        tx1.commit();
        session.close();

    }

    @Override
    public List<Lemma> findAllLemmas() {
        List<Lemma> lemmas = (List<Lemma>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Lemma ").list();
        return lemmas;
    }

    private boolean exist(String query){
        return (Lemma)HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession()
                .createQuery(query)
                .uniqueResult() !=null;
    }
}
