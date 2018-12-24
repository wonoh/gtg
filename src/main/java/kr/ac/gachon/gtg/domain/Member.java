package kr.ac.gachon.gtg.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "members")
@ToString
public class Member {
    @Id
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    private String id;

    private String email;
    @ToString.Exclude
    private String password;
    private String username;

    private String profile;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "member")
    private List<MemberRole> roles;

    @CreationTimestamp
    private LocalDateTime regdate;

    @UpdateTimestamp
    private LocalDateTime updatedate;
}
