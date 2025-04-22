package englishapp.api.exam_service.models;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionPart2 {
    @Field("id_question")
    private String idQuestion;
    @Field("audio")
    private String audio;
    @Field("correct_answer")
    @JsonIgnore
    private String correctAnswer;
}
