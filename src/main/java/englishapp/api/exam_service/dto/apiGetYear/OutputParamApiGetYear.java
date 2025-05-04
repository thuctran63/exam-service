package englishapp.api.exam_service.dto.apiGetYear;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamApiGetYear {
    public List<String> years;
}
