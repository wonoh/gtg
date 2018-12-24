package kr.ac.gachon.gtg.persistence;

import kr.ac.gachon.gtg.domain.Major;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends CrudRepository<Major, String> {
    Optional<Major> findByName(String name);
}
