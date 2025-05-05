package englishapp.api.exam_service.dto.apiGetHistory;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputParamApiGetHistory {
    public int numberOfTest;
    public int scoreAverage;
    public List<HistoryTestDTO> historyTestList;
}
