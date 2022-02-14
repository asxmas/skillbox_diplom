package searchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchapp.entity.Page;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository <Page, Integer> {

    List<Page> findPagesByIdIn(List<Integer> pageIds);

}
