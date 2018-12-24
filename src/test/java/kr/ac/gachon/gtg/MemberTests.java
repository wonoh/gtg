package kr.ac.gachon.gtg;

import kr.ac.gachon.gtg.domain.Member;
import kr.ac.gachon.gtg.domain.MemberRole;
import kr.ac.gachon.gtg.persistence.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberTests {
    @Autowired
    MemberRepository memberRepo;

    @Autowired
    PasswordEncoder gtgPasswordEncoder;

    @Test
    public void insertAdminTest() {
        MemberRole role = new MemberRole();
        role.setRoleName("ADMIN");

        Member member = new Member();
        member.setUsername("admin");
        member.setEmail("admin@gtg.gachon.ac.kr");
        member.setProfile("#");

        member.setRoles(Arrays.asList(role));
        member.setPassword(gtgPasswordEncoder.encode("admin"));

        memberRepo.save(member);
    }
}
