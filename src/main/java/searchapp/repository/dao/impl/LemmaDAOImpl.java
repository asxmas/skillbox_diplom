package searchapp.repository.dao.impl;

import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.config.HibernateSessionFactoryUtil;
import searchapp.entity.Lemma;

import java.util.List;
import java.util.Optional;

public class LemmaDAOImpl implements searchapp.repository.dao.LemmaDAO {


    @Override
    public Lemma findLemmaById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Lemma.class, id);
    }

    @Override
//    @Query("select l from Lemma l where l.lemmaName = :lemmaName")
    public Optional<Lemma> findLemmaByLemmaName(String lemmaName){

        try {
            HibernateSessionFactoryUtil
                    .getSessionFactory()
                    .openSession()
                    .createQuery("select 1 from Lemma l where l.lemmaName =: " + lemmaName)
                    .uniqueResult();
        } catch (QueryException ex){
            return Optional.empty();
        }
        return Optional.of((Lemma)HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession()
                .createQuery("select lem from Lemma lem where lem.lemmaName = :" + lemmaName)
                .uniqueResult());
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
