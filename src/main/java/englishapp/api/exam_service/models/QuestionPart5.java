package englishapp.api.exam_service.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class QuestionPart5 {
    @Field("list_question")
    private List<Question> listQuestion;
}
