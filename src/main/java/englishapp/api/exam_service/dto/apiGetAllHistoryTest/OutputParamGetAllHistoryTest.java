package englishapp.api.exam_service.dto.apiGetAllHistoryTest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamGetAllHistoryTest {
    private String testName;
    private String email;
    private String score;
    private String dateTest;
}
