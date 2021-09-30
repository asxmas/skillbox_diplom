package searchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import searchapp.entity.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {
}
