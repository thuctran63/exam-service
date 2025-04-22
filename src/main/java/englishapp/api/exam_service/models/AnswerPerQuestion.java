package englishapp.api.exam_service.models;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class AnswerPerQuestion {
    @Field("id_question")
    private String idQuestion;
    @Field("correct_answer")
    private String key;
}
