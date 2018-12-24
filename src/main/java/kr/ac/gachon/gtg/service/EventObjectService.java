package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.EventObject;

import java.util.ArrayList;

public interface EventObjectService {
    EventObject courseToEventObject(Course course, Integer index);

    ArrayList<EventObject> timetableToEventObjects(ArrayList<Course> timetable, Integer index);
}
