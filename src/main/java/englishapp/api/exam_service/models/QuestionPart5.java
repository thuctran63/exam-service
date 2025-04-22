package englishapp.api.exam_service.models;

import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionPart5 {
    @Field("question")
    private Question question;
}
