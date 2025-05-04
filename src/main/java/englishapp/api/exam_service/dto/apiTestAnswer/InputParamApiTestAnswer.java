package englishapp.api.exam_service.dto.apiTestAnswer;

import englishapp.api.exam_service.models.Answer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InputParamApiTestAnswer extends Answer {
    private String timeDoTest;
    private String dateTest;
}
