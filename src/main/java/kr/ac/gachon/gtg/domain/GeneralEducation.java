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
@Table(name = "gn")
@EqualsAndHashCode(of = "courseCode")
@ToString
public class GeneralEducation {
    @Id
    @Column(name = "cor_cd")
    private String courseCode;

    private String name;

    @OneToMany(mappedBy = "general", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Course> courses;
}
