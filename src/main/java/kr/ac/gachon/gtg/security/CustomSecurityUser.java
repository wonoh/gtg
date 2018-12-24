package kr.ac.gachon.gtg.security;

import kr.ac.gachon.gtg.domain.Member;
import kr.ac.gachon.gtg.domain.MemberRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomSecurityUser extends User {
    private static final String ROLE_PREFIX = "ROLE_";
    private Member member;

    public CustomSecurityUser(Member member) {
        super(member.getUsername(), member.getPassword(), makeGrantedAuthority(member.getRoles()));
        this.member = member;
    }

    private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles) {
        List<GrantedAuthority> list = new ArrayList<>();

        if (roles != null && !roles.isEmpty()) {
            roles.forEach(
                    role -> list.add(
                            new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName()))
            );
        } else {
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX + "USER"));
        }

        return list;
    }
}
