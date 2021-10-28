package legacy.service_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import searchapp.dao.PageDAO;
import searchapp.dao.impl.PageDAOimpl;
import searchapp.service.impl.PageServiceImpl;

import static org.junit.jupiter.api.Assertions.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageServiceTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    PageServiceImpl pageService;

    @Autowired
    PageDAO pageDAO = new PageDAOimpl();

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createPageTest() throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String startUrl = "http://www.playback.ru/warranty.html";
        pageService = new PageServiceImpl(startUrl);
        em.persist(pageService.createPage(startUrl));
        assertEquals(pageDAO.findAllPages().size(), 1);

    }

}
