package kr.ac.gachon.gtg.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventObject {
    private String title;
    // ISO8601 string
    private String start;
    // ISO8601 string
    private String end;
}
