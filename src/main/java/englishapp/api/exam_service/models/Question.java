package englishapp.api.exam_service.models;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class Question {
    @Field("id_question")
    private String idQuestion;
    @Field("question_text")
    private String questionText;
    @Field("answer_A")
    private String answerA;
    @Field("answer_B")
    private String answerB;
    @Field("answer_C")
    private String answerC;
    @Field("answer_D")
    private String answerD;
    @Field("correct_answer")
    private String correctAnswer;
}
