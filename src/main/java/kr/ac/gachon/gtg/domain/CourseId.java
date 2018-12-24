package kr.ac.gachon.gtg.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class CourseId implements Serializable {
    private Integer year;
    private Integer semester;
    private String code;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        CourseId cid = (CourseId) obj;
        if (this.year.equals(cid.year)) {
            if (this.semester.equals(cid.semester)) {
                return this.code.equals(cid.code);
            }
        }
        return false;
    }
}
