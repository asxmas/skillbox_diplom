package searchapp.dao;

import searchapp.entity.Page;

import java.util.List;

public interface PageDAO {
    Page findById(int id);

    void save(Page page);

    void update(Page page);

    void delete(Page user);

    List<Page> findAll();
}
