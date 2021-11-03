package searchapp.repository.dao;

import searchapp.entity.Index;

import java.util.List;

public interface IndexDAO {
    Index findIndexById(int id);

    void saveIndex(Index index);

    void updateIndex(Index index);

    void deleteIndex(Index index);

    List<Index> findAllIndexes();
}
