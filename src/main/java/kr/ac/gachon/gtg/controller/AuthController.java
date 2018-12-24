package kr.ac.gachon.gtg.controller;

import kr.ac.gachon.gtg.persistence.MemberRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth/")
@Log
public class AuthController {
    @Autowired
    MemberRepository memberRepo;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public void login(Model model) {
    }

    @PostMapping("/logout")
    public void logout() {
    }
}
