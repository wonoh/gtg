package kr.ac.gachon.gtg.service;

public interface CrawlingService {
    int SPRING_SEMESTER = 10;
    int FALL_SEMESTER = 20;

    void insertMajorCodes();

    void insertGeneralEducationCodes();

    void insertCourses(Integer year, Integer semester);
}
