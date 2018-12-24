package kr.ac.gachon.gtg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/")
public class UserController {
    private String pageTitle;
    private String breadcrumb;

    @GetMapping("/profile")
    public void profile(Model model) {
        pageTitle = "계정 정보";
        breadcrumb = "User Profile";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);
    }
}
