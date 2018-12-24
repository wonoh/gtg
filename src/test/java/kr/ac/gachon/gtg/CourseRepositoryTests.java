package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.CourseId;
import kr.ac.gachon.gtg.domain.Major;
import kr.ac.gachon.gtg.persistence.CourseRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
@Commit
public class CourseRepositoryTests {
    @Autowired
    CourseRepository repo;

    @Test
    public void insertCourses() {
        for (int i = 1; i < 10; i++) {
            Course course = new Course();

            CourseId id = new CourseId();
            id.setCode("1234900" + i);
            id.setYear(2018);
            id.setSemester(10);

            Major major = new Major();
            major.setUnivCode("CJ0000");
            major.setMajorCode("CJ0200");
            major.setName("컴퓨터공학과");

            course.setId(id);
            course.setTitle("종합프로젝트");
            course.setClassification("전필");
            course.setCredit(3);
            course.setQuota(40);
            course.setTime("월10, 월11, 월12, 월13");
            course.setInstructor("교수" + i);
            course.setRoom("IT-50" + i);
            course.setMajor(major);

            repo.save(course);
        }
    }

    @Test
    public void listCourses() {
        String maj_cd = "CJ0200";

//        repo.findCoursesByMajor(maj_cd)
//                .forEach(System.out::println);
    }

    @Test
    public void countCourses() {
        log.info(String.valueOf(repo.count()));
    }
}
