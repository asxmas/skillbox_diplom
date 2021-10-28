package searchapp.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchapp.config.HibernateSessionFactoryUtil;
import searchapp.entity.Lemma;

import java.util.List;
import java.util.Optional;

public class LemmaDAOImpl implements searchapp.dao.LemmaDAO {


    @Override
    public Lemma findLemmaById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Lemma.class, id);
    }

    @Override
    public Optional<Lemma> findLemmaByLemmaName(String lemmaName){
        return Optional.of(HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Lemma.class, lemmaName));
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
}
