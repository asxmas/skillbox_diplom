package legacy.service_test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import searchapp.repository.dao.impl.LemmaDAOImpl;
import searchapp.service.impl.LemmatizatorImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LemmatizatorTest {


    @Test
    void getLemmsTest(){
        String text = "Повторное появление леопарда в Осетии позволяет предположить, что леопард постоянно обитает в некоторых районах Северного Кавказа.";
        LemmatizatorImpl lemmatizator = new LemmatizatorImpl(new LemmaDAOImpl());
        Map <String, Integer> excepted = lemmatizator.getLemms(text);

        Map <String, Integer> actual = new HashMap<>();
        actual.put("повторный", 1);
        actual.put("появление", 1);
        actual.put("постоянно", 1);
        actual.put("позволять", 1);
        actual.put("предположить", 1);
        actual.put("северный", 1);
        actual.put("район", 1);
        actual.put("кавказ", 1);
        actual.put("осетия", 1);
        actual.put("леопард", 2);
        actual.put("обитать", 1);

        assertEquals(excepted, actual);

    }


}
