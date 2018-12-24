package kr.ac.gachon.gtg.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mj")
@EqualsAndHashCode(of = "majorCode")
@ToString
public class Major {
    @Id
    @Column(name = "maj_cd")
    private String majorCode;

    @Column(name = "univ_cd")
    private String univCode;
    private String name;

    @OneToMany(mappedBy = "major", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Course> courses;
}
