package englishapp.api.exam_service.dto.apiGetTestInfo;

import java.util.List;

import englishapp.api.exam_service.models.TestInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputParamApiGetTestInfo {
    private List<TestInfo> testInfoList;
}
