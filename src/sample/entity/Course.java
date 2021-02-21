package sample.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Course {
    private String title;
    private String startTime;
    private String endTime;
    private String count;
    private String duration;
    private String remaining;
    private String testId;
}
