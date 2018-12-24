package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.persistence.GeneralEducationRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class GeneralEducationRepoTests {
    @Autowired
    GeneralEducationRepository repo;

    @Test
    public void listGECodes() {
        repo.findAll()
                .forEach(ge -> log.info(ge.toString()));
    }
}
