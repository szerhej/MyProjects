package fg.eternity.bo;

import lombok.Data;

import java.util.List;

@Data
public class BoardDTO {
    private List<List<FigureVectorDTO>> figures;
}
