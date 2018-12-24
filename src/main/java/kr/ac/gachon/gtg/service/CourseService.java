package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.CourseId;

import java.util.ArrayList;

public interface CourseService {
    Course findCourseById(CourseId id);

    ArrayList<Course> findCoursesByTitle(Integer year, Integer semester, String title);

    ArrayList<Course> findCourses(Integer year, Integer semester, String code);

    ArrayList<Course> findCourses(Integer year, Integer semester, String grade, String major);
}
