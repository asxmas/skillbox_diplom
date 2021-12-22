package searchapp.repository.dao;

import searchapp.entity.Index;

import java.util.List;

public interface IndexDAO {
    Index findIndexById(int id);

    void saveIndex(Index index);

    void saveIndexes(List<Index> indexList);


    List<Index> findIndexesByLemmaId(Integer lemmaId);

    List<Index> findIndexesByPageIds(List<Integer> pageIds);

    List<Index> getIndexListForRelevance(List<Integer> pageIdList, List<Integer> lemmIdList);

    void updateIndex(Index index);

    void deleteIndex(Index index);

    List<Index> findAllIndexes();
}
