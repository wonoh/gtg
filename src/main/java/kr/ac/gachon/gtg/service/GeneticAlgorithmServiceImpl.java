package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.*;
import kr.ac.gachon.gtg.persistence.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

@Transactional
@Service("GeneticAlgorithmService")
public class GeneticAlgorithmServiceImpl implements GeneticAlgorithmService {
    @Autowired
    CourseRepository courseRepo;

    private ArrayList<Course> coursePool;
    private ArrayList<Timetable> population;
    private HashMap<Timetable, Integer> bestTimetable;

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATION_SIZE = 30;
    private static final double CROSSOVER_PROBABILITY = 0.8;
    private static final double MUTATION_PROBABILITY = 0.05;
    private static final int NUMBER_OF_RESULT = 6;

    private int TIMETABLE_SIZE;
    private int NUMBER_OF_MAJOR;
    private int MAX_CREDIT;
    private int GRADE;
    private int HOLIDAY;

    @Override
    public void setEnvironments(
            int year,
            int semester,
            String majorCode,
            int numberOfMajor,
            int credit,
            int grade,
            int holiday) {
        Major major = new Major();
        major.setMajorCode(majorCode);

        if (coursePool != null) {
            coursePool.clear();
        }
        coursePool = new ArrayList<>();
        courseRepo.findByMajor(major)
                .forEach(course -> {
                    CourseId id = course.getId();

                    if (id.getYear() == year && id.getSemester() == semester) {
                        this.coursePool.add(course);
                    }
                });

        if (population != null) {
            population.clear();
        }
        population = new ArrayList<>();

        if (bestTimetable != null) {
            bestTimetable.clear();
        }
        bestTimetable = new HashMap<>(GENERATION_SIZE);

        NUMBER_OF_MAJOR = numberOfMajor;
        TIMETABLE_SIZE = coursePool.size();
        MAX_CREDIT = credit;
        GRADE = grade;
        HOLIDAY = holiday;

        initializePopulation();
    }

    private ArrayList<Timetable> getPopulation() {
        return population;
    }

    private void initializePopulation() {
        Random random = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Timetable timetable = new Timetable(TIMETABLE_SIZE);

            while (timetable.getIndexList().size() < NUMBER_OF_MAJOR) {
                int index = random.nextInt(TIMETABLE_SIZE);

                if (!timetable.hasCourse(index)) {
                    timetable.addCourse(index);
                }
            }

            population.add(timetable);
        }
    }

    @Override
    public ArrayList<Course> getTimetableInfo(Timetable timetable) {
        ArrayList<Course> list = new ArrayList<>();

        timetable.getIndexList().forEach(index -> {
            list.add(coursePool.get(index));
        });

        return list;
    }

    @Override
    public ArrayList<Timetable> evolution() {
        for (int i = 0; i < GENERATION_SIZE; i++) {
            generation();

            Timetable best = new Timetable(getBest());
            bestTimetable.put(best, objectiveFunction(best));
        }

        Stream<Map.Entry<Timetable, Integer>> sorted =
                bestTimetable.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue());

        ArrayList<Timetable> result = new ArrayList<>();

        sorted.limit(NUMBER_OF_RESULT)
                .forEach(entry -> result.add(entry.getKey()));
        sorted.close();

        return result;
    }

    private Timetable getBest() {
        int best = 100;
        int index = 0;

        for (int i = 0; i < population.size(); i++) {
            Timetable temp = population.get(i);
            int score = objectiveFunction(temp);

            if (score < best) {
                best = score;
                index = i;
            }
        }

        return population.get(index);
    }

    private void generation() {
        ArrayList<Timetable> newPopulation = new ArrayList<>();

        // elitism
        newPopulation.add(getBest());

        while (newPopulation.size() < POPULATION_SIZE) {
            // selection
            int parentIndex1 = selection(getPopulation());
            int parentIndex2 = selection(getPopulation());

            // crossover
            ArrayList<Timetable> children =
                    crossover(getPopulation().get(parentIndex1), getPopulation().get(parentIndex2));

            // mutation
            children.set(0, mutation(children.get(0)));
            children.set(1, mutation(children.get(1)));

            // add children to the new population
            newPopulation.add(children.get(0));
            newPopulation.add(children.get(1));
        }

        // replace the population
        this.population = newPopulation;
    }

    private Integer selection(ArrayList<Timetable> population) {
        ArrayList<Integer> fitnessList = new ArrayList<>();

        for (Timetable timetable : population) {
            int value = objectiveFunction(timetable);

            fitnessList.add(value);
        }

        int best = fitnessList.stream().min(Integer::compareTo).get();
        int worst = fitnessList.stream().max(Integer::compareTo).get();

        for (int i = 0; i < population.size(); i++) {
            int value = fitnessList.get(i);
            value = (worst - value) + ((worst - best) / 3);

            fitnessList.set(i, value);
        }

        int sum = fitnessList.stream().mapToInt(Integer::intValue).sum();

        for (int i = 1; i < population.size(); i++) {
            int prevValue = fitnessList.get(i - 1);
            int value = fitnessList.get(i);

            fitnessList.set(i, prevValue + value);
        }

        Random random = new Random();
        int ball = random.nextInt(sum + 1);
        int index = 0;

        for (int i = 0; i < fitnessList.size(); i++) {
            index = i;

            if (ball <= fitnessList.get(i)) {
                break;
            }
        }

        return index;
    }

    private ArrayList<Timetable> crossover(Timetable parent1, Timetable parent2) {
        Random random = new Random();

        if (random.nextDouble() <= CROSSOVER_PROBABILITY) {
            int point = random.nextInt(parent1.getTableSize());

            ArrayList<Byte> tempTable = new ArrayList<>(parent1.getTable());

            for (int i = point; i < parent1.getTableSize(); i++) {
                Byte gene = parent2.getTable().get(i);

                parent1.getTable().set(i, gene);
                parent2.getTable().set(i, tempTable.get(i));
            }

            // calc timeMatrix
            countDuplicateTime(parent1);
            countDuplicateTime(parent2);
        }

        ArrayList<Timetable> result = new ArrayList<>();
        result.add(parent1);
        result.add(parent2);

        return result;
    }

    private Timetable mutation(Timetable timetable) {
        Random random = new Random();

        if (random.nextDouble() <= MUTATION_PROBABILITY) {
            int point = random.nextInt(timetable.getTableSize());
            byte gene = timetable.getTableValue(point);

            gene = (byte) ((gene == 0) ? 1 : 0);

            timetable.getTable().set(point, gene);
        }

        countDuplicateTime(timetable);

        return timetable;
    }

    private Integer objectiveFunction(Timetable timetable) {
        TimeMatrix.DayOfWeek day;

        switch (HOLIDAY) {
            case 1:
                day = TimeMatrix.DayOfWeek.MON;
                break;
            case 2:
                day = TimeMatrix.DayOfWeek.TUE;
                break;
            case 3:
                day = TimeMatrix.DayOfWeek.WED;
                break;
            case 4:
                day = TimeMatrix.DayOfWeek.THU;
                break;
            case 5:
                day = TimeMatrix.DayOfWeek.FRI;
                break;
            default:
                day = TimeMatrix.DayOfWeek.NULL;
                break;
        }

        int cdc = countDuplicateCourse(timetable) * 2;
        int cdt = countDuplicateTime(timetable) * 2;
        int dc = diffCredit(timetable) * 2;
        int dg = diffGrade(timetable) * 2;

        int result = cdc + cdt + dc + dg;

        if (day != TimeMatrix.DayOfWeek.NULL) {
            result += (hasHoliday(timetable, day) ? 0 : 1);
        }

        return result;
    }

    // 중복되는 강의 회수 체크
    private int countDuplicateCourse(Timetable timetable) {
        HashMap<String, Integer> map = new HashMap<>();

        timetable.getIndexList()
                .forEach(index -> {
                    Course course = coursePool.get(index);
                    String id = course.getId().getCode().substring(0, 5);

                    Integer count = map.get(id);

                    if (count == null) {
                        map.put(id, 1);
                    } else {
                        map.put(id, count + 1);
                    }
                });

        int count = 0;
        for (int value : map.values()) {
            if (value > 1) {
                count += value;
            }
        }

        return count;
    }

    // 중복되는 강의 시간 횟수 체크
    private int countDuplicateTime(Timetable timetable) {
        TimeMatrix timeMatrix = timetable.getTimeMatrix();
        timeMatrix.clearMatrix();

        timetable.getIndexList()
                .forEach(index -> {
                    Course course = coursePool.get(index);
                    timeMatrix.parseTimeString(course.getTime());
                });

        return timeMatrix.countDuplicate();
    }

    // 지정된 공강일에 과목이 없는지 체크
    private boolean hasHoliday(Timetable timetable, TimeMatrix.DayOfWeek holiday) {
        TimeMatrix timeMatrix = timetable.getTimeMatrix();

        for (int i = 0; i < timeMatrix.getColSize(); i++) {
            if (timeMatrix.getMatrixValue(holiday.ordinal(), i) != 0) {
                return false;
            }
        }

        return true;
    }

    // 학점 제한과 얼마나 차이있는지 검사
    private int diffCredit(Timetable timetable) {
        int totalCredit = 0;

        for (int index : timetable.getIndexList()) {
            Course course = coursePool.get(index);
            totalCredit += course.getCredit();
        }

        return Math.abs(MAX_CREDIT - totalCredit);
    }

    // 자신의 학년과 다른 학년의 수업이 얼마나 있는지 검사
    private int diffGrade(Timetable timetable) {
        int totalCourse = 0;

        for (int index : timetable.getIndexList()) {
            Course course = coursePool.get(index);

            if (!course.getGrade().contains(String.valueOf(GRADE))) {
                totalCourse++;
            }
        }

        return totalCourse;
    }
}
