package englishapp.api.exam_service.dto.apiTestAnswer;

import java.util.List;

import englishapp.api.exam_service.models.AnswerPerQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputParamApiTestAnswer {
    private String idTest;
    private List<AnswerPerQuestion> answersPart1;
    private List<AnswerPerQuestion> answersPart2;
    private List<AnswerPerQuestion> answersPart3;
    private List<AnswerPerQuestion> answersPart4;
    private List<AnswerPerQuestion> answersPart5;
    private List<AnswerPerQuestion> answersPart6;
    private List<AnswerPerQuestion> answersPart7;
}
