package englishapp.api.exam_service.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Test")
public class Test {
    @Id
    private String idTest;
    private List<QuestionPart1> questionPart1;
    private List<QuestionPart2> questionPart2;
    private List<QuestionPart3> questionPart3;
    private List<QuestionPart4> questionPart4;
    private List<QuestionPart5> questionPart5;
    private List<QuestionPart6> questionPart6;
    private List<QuestionPart7> questionPart7;
}
