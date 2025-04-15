package englishapp.api.exam_service.models;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class QuestionPart1 {
    @Field("id_question")
    private String idQuestion;
    @Field("image")
    private String image;
    @Field("audio")
    private String audio;
    @Field("correct_answer")
    private String correctAnswer;
}
