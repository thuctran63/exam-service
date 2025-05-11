package englishapp.api.exam_service.dto.apiGetTest;

import englishapp.api.exam_service.models.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class OutputParamApiGetTest extends Test {
    private String nameTest;
}
