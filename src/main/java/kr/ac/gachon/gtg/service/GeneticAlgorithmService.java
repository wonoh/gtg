package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.Timetable;

import java.util.ArrayList;

public interface GeneticAlgorithmService {
    void setEnvironments(int year, int semester, String majorCode, int numberOfMajor, int credit, int grade,
                         int holiday);

    ArrayList<Timetable> evolution();

    ArrayList<Course> getTimetableInfo(Timetable timetable);
}
