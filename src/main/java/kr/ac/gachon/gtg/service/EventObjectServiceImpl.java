package kr.ac.gachon.gtg.service;


import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.EventObject;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Service("EventObjectService")
public class EventObjectServiceImpl implements EventObjectService {
    @Override
    public EventObject courseToEventObject(Course course, Integer index) {
        return null;
    }

    @Override
    public ArrayList<EventObject> timetableToEventObjects(ArrayList<Course> timetable, Integer index) {
        ArrayList<EventObject> eventObjectList = new ArrayList<>();

        timetable.forEach(course -> {
            String[] timeList = course.getTime().replaceAll(" ", "").split(",");

            EventObject current = null;
            String start = "";
            String end = "";

            for (int i = 0; i < timeList.length; i++) {
                if (current == null) {
                    current = new EventObject();
                    current.setTitle(course.getTitle() + "-" + course.getInstructor());
                    start = timeList[i];
                }

                if (i == timeList.length - 1 || timeList[i].charAt(0) != timeList[i + 1].charAt(0)) {
                    end = timeList[i];

                    current.setStart(start);
                    current.setEnd(end);

                    eventObjectList.add(current);
                    current = null;
                }
            }
        });

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+9:00"));
        ZonedDateTime now = ZonedDateTime.now().plusWeeks(index);
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        eventObjectList.forEach(eventObject -> {
            String start = eventObject.getStart();
            String end = eventObject.getEnd();

            eventObject.setStart(parseStartTime(now, today, start));
            eventObject.setEnd(parseEndTime(now, today, end));
        });

        return eventObjectList;
    }

    private String parseStartTime(ZonedDateTime now, int dayOfWeek, String start) {
        now = now.truncatedTo(ChronoUnit.DAYS);
        int days = 0;
        int hours = 0;
        int mins = 0;

        switch (start.charAt(0)) {
            case '월':
                days = Calendar.MONDAY - dayOfWeek;
                break;
            case '화':
                days = Calendar.TUESDAY - dayOfWeek;
                break;
            case '수':
                days = Calendar.WEDNESDAY - dayOfWeek;
                break;
            case '목':
                days = Calendar.THURSDAY - dayOfWeek;
                break;
            case '금':
                days = Calendar.FRIDAY - dayOfWeek;
                break;
        }

        String classTime = start.substring(1);

        // 50 minutes class
        if (classTime.matches("\\d+")) {
            int plus = Integer.parseInt(classTime);
            int base = 8;

            // night class
            if (plus >= 9) {
                hours = base + 9;
                mins = 30;
                mins += (55 * (plus - 9));
            } else {
                hours = base + plus;
            }
        } else {
            switch (classTime) {
                case "A":
                    hours = 9;
                    mins = 30;
                    break;
                case "B":
                    hours = 11;
                    mins = 0;
                    break;
                case "C":
                    hours = 12;
                    mins = 30;
                    break;
                case "D":
                    hours = 14;
                    mins = 0;
                    break;
                case "E":
                    hours = 15;
                    mins = 30;
            }
        }

        return now.plusDays(days)
                .plusHours(hours)
                .plusMinutes(mins)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String parseEndTime(ZonedDateTime today, int dayOfWeek, String end) {
        today = today.truncatedTo(ChronoUnit.DAYS);

        String start = parseStartTime(today, dayOfWeek, end);
        String classTime = end.substring(1);
        int mins = 0;

        // 50 minutes class
        if (classTime.matches("\\d+")) {
            mins = 50;
        } else {
            mins = 75;
        }

        return ZonedDateTime.parse(start + "+09:00")
                .plusMinutes(mins)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
