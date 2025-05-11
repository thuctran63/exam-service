package englishapp.api.exam_service.dto.apiGetAllTest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamApiGetAllTest {
    private String idTest;
    private String testName;
    private String numberOfQuestion;
}
