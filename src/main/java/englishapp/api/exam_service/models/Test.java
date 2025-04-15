package englishapp.api.exam_service.models;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Test {
    @Id
    private String id;
    private String testName;
    private String year;
    private List<QuestionPart1> questionPart1;
    private List<QuestionPart2> questionPart2;
    private List<QuestionPart3> questionPart3;
    private List<QuestionPart5> questionPart5;
    private List<QuestionPart6> questionPart6;
    private List<QuestionPart7> questionPart7;
}
