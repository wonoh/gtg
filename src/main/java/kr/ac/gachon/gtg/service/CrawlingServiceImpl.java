package kr.ac.gachon.gtg.service;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.CourseId;
import kr.ac.gachon.gtg.domain.GeneralEducation;
import kr.ac.gachon.gtg.domain.Major;
import kr.ac.gachon.gtg.persistence.CourseRepository;
import kr.ac.gachon.gtg.persistence.GeneralEducationRepository;
import kr.ac.gachon.gtg.persistence.MajorRepository;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("CrawlingService")
@Log
public class CrawlingServiceImpl implements CrawlingService {
    private CourseRepository courseRepo;
    private GeneralEducationRepository geRepo;
    private MajorRepository mjRepo;

    private String targetUrl;

    private static Map<String, String> body = new HashMap<>();

    @Autowired
    public CrawlingServiceImpl(CourseRepository courseRepo,
                               GeneralEducationRepository geRepo,
                               MajorRepository mjRepo,
                               @Value("${crawling.target.url}") String target) {
        this.courseRepo = courseRepo;
        this.geRepo = geRepo;
        this.mjRepo = mjRepo;
        this.targetUrl = target;

        bodyInitializer();
    }

    private void bodyInitializer() {
        body.put("attribute", "");
        body.put("flag", "A");
        body.put("lang", "ko");
        body.put("year", "");
        body.put("hakgi", "");
        body.put("cor_cd", "");
        body.put("univ_cd", "");
        body.put("maj_cd", "");
        body.put("isu_cd", "");
    }

    @Override
    public void insertMajorCodes() {
        Document doc = null;
        String url = targetUrl + "?attribute=top&lang=ko";

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The code of searching majors is 1
        body.put("isu_cd", "1");
        body.put("attribute", "top");
        url = targetUrl;

        for (Element opt : doc.select("select[name='univ_cd']").select("option")) {
            if (opt.text().contains("폐기")) {
                continue;
            }
            body.put("univ_cd", opt.attr("value"));

            try {
                doc = Jsoup.connect(url)
                        .data(body)
                        .post();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Element subOpt : doc.select("select[name='maj_cd']").select("option")) {
                String name = subOpt.text();

                if (name.contains("폐기") || name.contains("(야)")) {
                    continue;
                }

                Major major = new Major();
                // 1: univ_cd, 2: maj_cd, 3: name
                major.setUnivCode(opt.attr("value"));
                major.setMajorCode(subOpt.attr("value"));
                major.setName(subOpt.text());

                log.info("[학과코드 객체]: " + major);
                mjRepo.save(major);
            }
        }
    }

    @Override
    public void insertGeneralEducationCodes() {
        Document doc = null;
        String url = targetUrl + "?attribute=top&lang=ko";

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Element opt : doc.select("select[name='cor_cd']").select("option")) {
            if (opt.text().contains("폐기")) {
                continue;
            }

            GeneralEducation ge = new GeneralEducation();
            // 1: cor_cd, 2: name
            ge.setCourseCode(opt.attr("value"));
            ge.setName(opt.text());

            log.info("[교양코드 객체]: " + ge);
            geRepo.save(ge);
        }
    }

    @Override
    public void insertCourses(Integer year, Integer semester) {
        insertMajorCourses(year, semester);
        insertGeneralEduCourses(year, semester);
    }

    public void insertMajorCourses(Integer y, Integer s) {
        Document topDoc = null;
        String url = targetUrl + "?attribute=top&lang=ko";

        // Use Jsoup for crawling
        try {
            topDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        url = targetUrl + "?attribute=lists";

        // Insert loop
        for (Element yearOpt : topDoc.select("select[name='year']").select("option")) {
            Integer year = Integer.valueOf(yearOpt.attr("value"));

            if (!year.equals(y))
                break;
            log.info(String.format("[크롤링-전공] %d년 시작", year));

            for (Element hakgiOpt : topDoc.select("select[name='hakgi']").select("option")) {
                Integer hakgi = Integer.valueOf(hakgiOpt.attr("value"));

                if (!hakgi.equals(s))
                    continue;
                log.info(String.format("[크롤링-전공] %d학기 시작", hakgi));

                bodyInitializer();
                body.put("year", year.toString());
                body.put("hakgi", hakgi.toString());

                // Major classification code
                body.put("isu_cd", "1");

                String finalUrl = url;
                mjRepo.findAll()
                        .forEach(major -> {
                            log.info("[크롤링-전공] " + major.getName() + " 시작!");

                            body.put("univ_cd", major.getUnivCode());
                            body.put("maj_cd", major.getMajorCode());

                            Document listDoc = null;

                            try {
                                listDoc = Jsoup.connect(finalUrl)
                                        .data(body)
                                        .post();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Elements table = listDoc.select("table")
                                    .select("table")
                                    .eq(2);
                            Elements rows = table.select("tr");

                            log.info("[크롤링-전공] 현재 학과 과목 수: " +
                                    String.valueOf((rows.size() <= 2 ? 0 : rows.size() - 1)));

                            // check the result is empty(le 2 rows) or not(ge 3)
                            if (rows.size() > 2) {
                                // insert the subjects
                                for (Element tr : rows) {

                                    // Pass column labels
                                    if (tr.elementSiblingIndex() == 0)
                                        continue;

                                    Elements td = tr.select("td");

                                    // year, semester, code, title, classification, credit,
                                    // quota, time, instructor, room, grade, syllabus, maj_cd, cor_cd (14 columns)
                                    CourseId courseId = new CourseId();
                                    courseId.setYear(year);
                                    courseId.setSemester(hakgi);
                                    // pass the index 0(row number of table)
                                    courseId.setCode(td.eq(1).text());

                                    Course course = new Course();
                                    course.setId(courseId);
                                    course.setTitle(td.eq(2).text());
                                    // pass the index 3(trailer link)
                                    course.setClassification(td.eq(4).text());
                                    course.setCredit(Integer.valueOf(td.eq(5).text()));
                                    course.setQuota(Integer.valueOf(td.eq(6).text()));
                                    course.setTime(td.eq(7).text());
                                    course.setInstructor(td.eq(8).text());
                                    course.setRoom(td.eq(9).text());
                                    course.setGrade(td.eq(10).text());
                                    course.setSyllabus(td.eq(1).select("a").attr("href"));
                                    course.setMajor(major);

                                    courseRepo.save(course);
                                }
                            }

                            log.info("[크롤링-전공] " + major.getName() + " 종료!");
                        });
                // end majRepo foreach
            }
            // end hakgi for-loop
        }
        // end year for-loop
    }

    public void insertGeneralEduCourses(Integer y, Integer s) {
        Document topDoc = null;
        String url = targetUrl + "?attribute=top&lang=ko";

        // Use Jsoup for crawling
        try {
            topDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        url = targetUrl + "?attribute=lists";

        // Insert loop
        for (Element yearOpt : topDoc.select("select[name='year']").select("option")) {
            Integer year = Integer.valueOf(yearOpt.attr("value"));

            if (!year.equals(y))
                break;
            log.info(String.format("[크롤링-교양] %d년 시작", year));

            for (Element hakgiOpt : topDoc.select("select[name='hakgi']").select("option")) {
                Integer hakgi = Integer.valueOf(hakgiOpt.attr("value"));

                if (!hakgi.equals(s))
                    continue;
                log.info(String.format("[크롤링-교양] %d학기 시작", hakgi));

                bodyInitializer();
                body.put("year", year.toString());
                body.put("hakgi", hakgi.toString());

                // The classification code for general educations
                body.put("isu_cd", "2");

                String finalUrl = url;
                geRepo.findAll()
                        .forEach(ge -> {
                            log.info("[크롤링-교양] " + ge.getName() + " 시작!");

                            body.put("cor_cd", ge.getCourseCode());

                            Document listDoc = null;

                            try {
                                listDoc = Jsoup.connect(finalUrl)
                                        .data(body)
                                        .post();
                            } catch (IOException e) {
                                System.err.println(e.getMessage());
                            }

                            Elements table = listDoc.select("table")
                                    .select("table")
                                    .eq(2);
                            Elements rows = table.select("tr");

                            log.info("[크롤링-교양] 현재 학과 과목 수: " +
                                    String.valueOf((rows.size() <= 2 ? 0 : rows.size() - 1)));

                            // check the result is empty(le 2 rows) or not(ge 3)
                            if (rows.size() > 2) {
                                // insert the subjects
                                for (Element tr : rows) {

                                    // Pass column labels
                                    if (tr.elementSiblingIndex() == 0)
                                        continue;

                                    Elements td = tr.select("td");

                                    // year, semester, code, title, classification, credit,
                                    // quota, time, instructor, room, grade, syllabus, maj_cd, cor_cd (14 columns)
                                    CourseId courseId = new CourseId();
                                    courseId.setYear(year);
                                    courseId.setSemester(hakgi);
                                    // pass the index 0(row number of table)
                                    courseId.setCode(td.eq(1).text());

                                    Course course = new Course();
                                    course.setId(courseId);
                                    course.setTitle(td.eq(2).text());
                                    // pass the index 3(trailer link)
                                    course.setClassification(td.eq(4).text());
                                    course.setCredit(Integer.valueOf(td.eq(5).text()));
                                    course.setQuota(Integer.valueOf(td.eq(6).text()));
                                    course.setTime(td.eq(7).text());
                                    course.setInstructor(td.eq(8).text());
                                    course.setRoom(td.eq(9).text());
                                    course.setGrade(td.eq(10).text());
                                    course.setSyllabus(td.eq(1).select("a").attr("href"));
                                    course.setGeneral(ge);

                                    courseRepo.save(course);
                                }
                            }

                            log.info("[크롤링-교양] " + ge.getName() + " 종료!");
                        });
                // end geRepo foreach
            }
            // end hakgi for-loop
        }
        // end year for-loop
    }
}
