package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.service.CourseService;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class CourseServiceTests {
    @Autowired
    CourseService service;

    @Test
    public void findCoursesTest() {
        service.findCourses(2018, 10, "3", "CJ0200")
                .forEach(course -> log.info("검색 결과: " + course.toString()));
    }

    @Test
    public void findCoursesTest2() {
        service.findCourses(2018, 10, "CJ0200")
                .forEach(course -> log.info(course.toString()));
    }
}
