package englishapp.api.exam_service.dto.apiGetTestHistory;

import java.util.List;

import englishapp.api.exam_service.models.AnswerPerQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamApiGetTestHistory {

    private String nameTest;
    private String score;
    private String scoreListening;
    private String scoreReading;
    private UserAnswer userAnswer;
    private UserAnswer correctAnswer;
    private String dateTest;
    private String timeTest;

    @Data
    @AllArgsConstructor
    public static class UserAnswer {
        private List<AnswerPerQuestion> answersPart1;
        private List<AnswerPerQuestion> answersPart2;
        private List<AnswerPerQuestion> answersPart3;
        private List<AnswerPerQuestion> answersPart4;
        private List<AnswerPerQuestion> answersPart5;
        private List<AnswerPerQuestion> answersPart6;
        private List<AnswerPerQuestion> answersPart7;
    }
}
