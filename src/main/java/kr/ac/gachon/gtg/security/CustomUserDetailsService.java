package kr.ac.gachon.gtg.security;

import kr.ac.gachon.gtg.persistence.MemberRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("LoadUserByUsername: " + s);

        return memberRepo.findByEmail(s)
                .map(CustomSecurityUser::new)
                .get();
    }
}
