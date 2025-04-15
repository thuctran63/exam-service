package englishapp.api.exam_service.models;

import org.springframework.data.mongodb.core.mapping.Field;

public class QuestionPart2 {
    @Field("id_question")
    private String idQuestion;
    @Field("audio")
    private String audio;
    @Field("correct_answer")
    private String correctAnswer;
}
