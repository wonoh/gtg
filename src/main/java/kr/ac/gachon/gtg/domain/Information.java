package kr.ac.gachon.gtg.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Information {
    private String attribute;
    private String value;
    private String comment;

    public Information() {
    }

    public Information(String attribute, String value, String comment) {
        this.attribute = attribute;
        this.value = value;
        this.comment = comment;
    }
}
