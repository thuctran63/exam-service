package englishapp.api.exam_service.dto.apiGetHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTestDTO {
    private String idTest;
    private String nameTest;
    private String dateTest;
    private String scoreTest;
}
