package fg.eternity.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fg.eternity.bo.BoardDTO;
import fg.eternity.bo.FigureVectorDTO;
import fg.eternity.bo.GoalDTO;
import fg.eternity.util.BoardParser;
import fg.eternity.util.LangUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller class of Eternity Service
 */
@Controller
@RequestMapping("/services")
@Slf4j
public class EternityServiceController {

    @Value("${fg.eternity.goal.targetFolder}")
    private String sourceFolder;


    private ObjectMapper objectMapper;

    @PostConstruct
    public void init(){
        objectMapper=new ObjectMapper();
    }

    @Autowired
    private EternityService eternityService;

    @Autowired
    private BoardParser boardParser;

    /**
     * Returns "Hello World!!"
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public @ResponseBody String getHello() {
        return "Hello World!!";
    }

    /**
     * Returns all countries
     * @return
     */
    @RequestMapping(value = "/boards/{boardId}", method = RequestMethod.GET)
    public @ResponseBody List<List<FigureVectorDTO>> queryBoard(@PathVariable(name = "boardId") String boardId) throws Exception {
        try(InputStream inputStream = new FileInputStream(sourceFolder+ File.separator+boardId)){
            GoalDTO goalDTO = objectMapper.readValue(IOUtils.toString(inputStream,"UTF8"),GoalDTO.class);
            return boardParser.parse(goalDTO.getBoardTxt());
        }
    }

    /**
     * Returns all results
     * @return
     */
    @RequestMapping(value = "/boards", method = RequestMethod.GET)
    public @ResponseBody List<GoalDTO> queryResults() throws Exception {
        File sourceFolderFile = new File(sourceFolder);
        List<GoalDTO> goalDTOS = new ArrayList<>(1000);
        for(String fileName:new File(sourceFolder).list()){
            File file = new File(sourceFolderFile,fileName);
            try(FileInputStream fi=new FileInputStream(file)){
                String sourceTxt = IOUtils.toString(fi,"UTF8");
                GoalDTO goalDTO = objectMapper.readValue(sourceTxt,GoalDTO.class);
                goalDTO.setId(fileName);
                goalDTOS.add(goalDTO);
            }
        }
        Collections.sort(goalDTOS,(o1, o2) -> StringUtils.compare(o1.getId(),o2.getId()));
        return goalDTOS;
    }



}
