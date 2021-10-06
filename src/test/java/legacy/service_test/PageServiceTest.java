package legacy.service_test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import searchapp.DAO.PageDAO;
import searchapp.service.impl.PageServiceImpl;

import javax.persistence.EntityManagerFactory;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageServiceTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    PageServiceImpl pageService;

    @Autowired
    PageDAO pageDAO;

}
