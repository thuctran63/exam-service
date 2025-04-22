package englishapp.api.exam_service.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "Answers")
public class Answer {
    @Id
    private String idTest;
    private List<AnswerPerQuestion> answersPart1;
    private List<AnswerPerQuestion> answersPart2;
    private List<AnswerPerQuestion> answersPart3;
    private List<AnswerPerQuestion> answersPart4;
    private List<AnswerPerQuestion> answersPart5;
    private List<AnswerPerQuestion> answersPart6;
    private List<AnswerPerQuestion> answersPart7;
}
