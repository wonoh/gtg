package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.persistence.MajorRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class MajorRepositoryTests {
    @Autowired
    MajorRepository repo;

    @Test
    public void listMajorCodes() {
        repo.findAll()
                .forEach(mj -> log.info(mj.toString()));
    }

    @Test
    public void findMajorCodes() {
        repo.findByName("컴퓨터공학과")
                .ifPresent(major -> log.info(major.toString()));
    }

    @Transactional
    @Test
    public void findCourseByMajor() {
        repo.findById("CJ0200")
                .ifPresent(major -> log.info(major.getCourses().toString()));
    }
}
