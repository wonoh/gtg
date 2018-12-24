package kr.ac.gachon.gtg.controller;

import kr.ac.gachon.gtg.domain.Course;
import kr.ac.gachon.gtg.domain.Information;
import kr.ac.gachon.gtg.persistence.CourseRepository;
import kr.ac.gachon.gtg.persistence.MemberRepository;
import kr.ac.gachon.gtg.service.CrawlingService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/manage/")
@Log
public class ManageController {

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    MemberRepository memberRepo;

    @Autowired
    CrawlingService crawlingService;

    private String pageTitle;
    private String breadcrumb;

    @GetMapping("/database")
    public void database(HttpServletRequest request, Model model) {
        pageTitle = "데이터베이스 관리";
        breadcrumb = "Manage";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);

        ArrayList<Information> infoList = new ArrayList<>();

        // Total Courses
        Information totalCourses = new Information("저장되어 있는 강의 수",
                String.valueOf(courseRepo.count()), "");
        infoList.add(totalCourses);

        // Total Majors
        Information totalMajors = new Information("저장되어 있는 전공강의 수",
                String.valueOf(courseRepo.countByMajorIsNotNull()), "");
        infoList.add(totalMajors);

        // Total GeneralEducation
        Information totalGenerals = new Information("저장되어 있는 교양강의 수",
                String.valueOf(courseRepo.countByGeneralIsNotNull()), "");
        infoList.add(totalGenerals);

        // Most recent course update
        String updateTime = courseRepo.findTopByUpdateTimeIsNotNullOrderByUpdateTimeDesc()
                .map(Course::getUpdateTime)
                .map(LocalDateTime::toString)
                .orElse("null");

        Information mostRecentUpdate = new Information("가장 최근 업데이트 일", updateTime, "");
        infoList.add(mostRecentUpdate);

        // Total Users
        Information totalUsers = new Information("현재 계정 수",
                String.valueOf(memberRepo.count()), "");
        infoList.add(totalUsers);

        model.addAttribute("infoList", infoList);
    }

    @PostMapping("/database")
    public String databasePost(HttpServletRequest request,
                               @RequestParam Map<String, String> body,
                               Model model) {

        String action = body.get("action");

        if (action.equals("insert")) {
            Integer year = null;
            Integer semester = null;

            try {
                year = Integer.parseInt(body.get("year"));
                semester = Integer.parseInt(body.get("semester"));

                crawlingService.insertCourses(year, semester);
            } catch (NumberFormatException nfe) {
                System.err.println("Unexpected params!");
                System.err.println(nfe.getMessage());
            }
        } else if (action.equals("delete")) {
            courseRepo.deleteAll();
        }

        return "redirect:database";
    }

    @GetMapping("/user")
    public void user(HttpServletRequest request, Model model) {
        pageTitle = "계정 관리";
        breadcrumb = "Manage";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);
    }

    @GetMapping("/setting")
    public void setting(HttpServletRequest request, Model model) {
        pageTitle = "Settings";
        breadcrumb = "Manage";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);
    }
}
