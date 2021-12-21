package searchapp.repository.dao;

import searchapp.entity.Page;

import java.util.List;

public interface PageDAO {
    Page findPageById(int id);

    List<Page> findPagesByIds(List<Integer> pageIds);

    void savePage(Page page);

    void updatePage(Page page);

    void deletePage(Page user);

    List<Page> findAllPages();
}
