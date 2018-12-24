package kr.ac.gachon.gtg.persistence;

import kr.ac.gachon.gtg.domain.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, String> {
    Optional<Member> findByEmail(String email);
}
