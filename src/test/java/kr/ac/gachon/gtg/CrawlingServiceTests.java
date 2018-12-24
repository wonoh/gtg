package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.service.CrawlingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlingServiceTests {

    @Autowired
    CrawlingService crawlingService;

    @Test
    public void insertMajorCodesTest() {
        crawlingService.insertMajorCodes();
    }

    @Test
    public void insertGeneralEducationCodesTest() {
        crawlingService.insertGeneralEducationCodes();
    }

    @Test
    public void insertCoursesTest() {
        crawlingService.insertCourses(2018, CrawlingService.SPRING_SEMESTER);
    }
}
