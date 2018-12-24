package kr.ac.gachon.gtg.persistence;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.CourseId;
import kr.ac.gachon.gtg.domain.Major;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, CourseId> {
    Iterable<Course> findCoursesByTitleContaining(String title);

    Iterable<Course> findCoursesByGradeContainingAndMajor(String grade, Major major);

    Iterable<Course> findByMajor(Major major);

    Long countByMajorIsNotNull();

    Long countByGeneralIsNotNull();

    Optional<Course> findTopByUpdateTimeIsNotNullOrderByUpdateTimeDesc();
}
