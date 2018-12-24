package kr.ac.gachon.gtg.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "course")
@EqualsAndHashCode(of = "id")
@ToString
public class Course {

    @EmbeddedId
    private CourseId id;

    private String title;
    private String classification;
    private Integer credit;
    private Integer quota;
    private String time;
    private String instructor;
    private String room;
    private String grade;
    private String syllabus;

    @ManyToOne
    @JoinColumn(name = "maj_cd")
    @ToString.Exclude
    private Major major;

    @ManyToOne
    @JoinColumn(name = "cor_cd")
    @ToString.Exclude
    private GeneralEducation general;

    @CreationTimestamp
    private LocalDateTime regTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
