package englishapp.api.exam_service.models;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;

@Data
public class QuestionPart3 {
    @Field("image")
    private String image;
    @Field("audio")
    private String audio;
    @Field("list_question")
    private List<Question> listQuestion;
}
