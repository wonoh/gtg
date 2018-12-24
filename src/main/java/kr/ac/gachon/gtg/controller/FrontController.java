package kr.ac.gachon.gtg.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log
public class FrontController {
    private String pageTitle;
    private String breadcrumb;

    @GetMapping("/")
    public String index(Model model) {
        pageTitle = "Home";
        breadcrumb = "Home";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);

        return "index";
    }

    @GetMapping("/admin")
    public void admin() {

    }

    @GetMapping("/user")
    public void user(Model model) {
    }

    @GetMapping("/accessDenied")
    public String error(Model model) {
        pageTitle = "404 Error Page";
        breadcrumb = "404 Error Page";

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("breadcrumb", breadcrumb);

        return "404";
    }
}
