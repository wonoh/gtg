package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.CourseId;
import kr.ac.gachon.gtg.domain.Major;
import kr.ac.gachon.gtg.persistence.CourseRepository;
import kr.ac.gachon.gtg.persistence.GeneralEducationRepository;
import kr.ac.gachon.gtg.persistence.MajorRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service("CourseService")
@Log
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private MajorRepository majorRepo;

    @Autowired
    private GeneralEducationRepository generalEducationRepo;

    @Override
    public Course findCourseById(CourseId id) {
        Course course = new Course();

        courseRepo.findById(id).ifPresent(c -> {
           course.setId(c.getId());
           course.setTitle(c.getTitle());
           course.setTime(c.getTime());
           course.setInstructor(c.getInstructor());
           course.setRoom(c.getRoom());
           course.setSyllabus(c.getSyllabus());
        });

        return course;
    }

    @Override
    public ArrayList<Course> findCoursesByTitle(Integer year, Integer semester, String title) {
        ArrayList<Course> list = new ArrayList<>();

        courseRepo.findCoursesByTitleContaining(title)
                .forEach(course -> {
                    CourseId id = course.getId();

                    if (id.getYear().equals(year) && id.getSemester().equals(semester)) {
                        list.add(course);
                    }
                });

        return list;
    }

    @Transactional
    @Override
    public ArrayList<Course> findCourses(Integer year, Integer semester, String code) {
        ArrayList<Course> list = new ArrayList<>();

        switch (code.length()) {
            case 3:
                generalEducationRepo.findById(code)
                        .ifPresent(ge -> {
                            ge.getCourses()
                                    .forEach(course -> {
                                        CourseId id = course.getId();

                                        if (id.getYear().equals(year) &&
                                                id.getSemester().equals(semester))
                                            list.add(course);
                                    });
                        });
                break;
            case 6:
                majorRepo.findById(code)
                        .ifPresent(m -> {
                            m.getCourses()
                                    .forEach(course -> {
                                        CourseId id = course.getId();

                                        if (id.getYear().equals(year) &&
                                                id.getSemester().equals(semester))
                                            list.add(course);
                                    });
                        });
                break;
            default:
        }

        return list;
    }

    @Override
    public ArrayList<Course> findCourses(Integer year, Integer semester, String grade, String major) {
        ArrayList<Course> list = new ArrayList<>();

        Major mj = new Major();
        mj.setMajorCode(major);

        courseRepo.findCoursesByGradeContainingAndMajor(grade, mj)
                .forEach(course -> {
                    CourseId id = course.getId();

                    if (id.getYear().equals(year) && id.getSemester().equals(semester))
                        list.add(course);
                });

        return list;
    }
}
