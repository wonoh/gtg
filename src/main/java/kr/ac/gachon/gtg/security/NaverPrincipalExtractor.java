package kr.ac.gachon.gtg.security;

import kr.ac.gachon.gtg.domain.Member;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.HashMap;
import java.util.Map;

@Log
public class NaverPrincipalExtractor implements PrincipalExtractor {
    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        Member member = new Member();

        log.info("Naver Response: " + map.toString());

        if (map.get("message").equals("success")) {
            HashMap<String, String> response = (HashMap<String, String>) map.get("response");

            member.setId(response.get("id"));
            member.setEmail(response.get("email"));
            member.setUsername(response.get("nickname"));
            member.setProfile(response.get("profile_image"));
            member.setPassword("");

            return new CustomSecurityUser(member);
        }

        return null;
    }
}
