package searchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchapp.entity.Index;
import searchapp.entity.Lemma;
import searchapp.entity.Page;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository <Index, Integer> {

    List<Index> findAllByLemma(Lemma lemma);

    List<Index> findAllByPageIn(List<Page> pages);

//    List<Index> getIndexListForRelevance(List<Integer> pageIdList, List<Integer> lemmIdList);
}
