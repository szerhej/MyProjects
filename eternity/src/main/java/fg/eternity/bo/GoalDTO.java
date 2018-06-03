package fg.eternity.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
public class GoalDTO {
    @NonNull
    private String name;
    @NonNull
    private String boardTxt;
    @NonNull
    private Date createTS = new Date();
    private long absoluteLevel;
}
