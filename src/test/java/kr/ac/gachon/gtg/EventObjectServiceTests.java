package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.service.EventObjectService;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
public class EventObjectServiceTests {
    @Autowired
    EventObjectService eventObjectService;

    @Test
    public void eventTest() {

        ArrayList<Course> list = new ArrayList<>();

        Course c1 = new Course();
        c1.setTitle("과목1");
        c1.setTime("월1 ,월2 ,월3 ,월4");

        Course c2 = new Course();
        c2.setTitle("과목2");
        c2.setTime("목A ,금B");

        Course c3 = new Course();
        c3.setTitle("과목3");
        c3.setTime("화10 ,화11 ,화12 ,화13");

        list.add(c1);
        list.add(c2);
        list.add(c3);

        eventObjectService.timetableToEventObjects(list, 0)
                .forEach(eventObject -> log.info(eventObject.toString()));
    }
}
