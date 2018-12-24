package kr.ac.gachon.gtg.persistence;

import kr.ac.gachon.gtg.domain.GeneralEducation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneralEducationRepository extends CrudRepository<GeneralEducation, String> {
    public Optional<GeneralEducation> findByName(String name);
}
