package englishapp.api.exam_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "TestInfo")
public class TestInfo {
    @Id
    private String idTest;
    private String testName;
    private String year;
}
