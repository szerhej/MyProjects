package fg.eternity.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fg.eternity.bo.FigureVectorDTO;
import fg.eternity.bo.GoalDTO;
import fg.eternity.util.BoardParser;
import fg.eternity.util.LangUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Controller class of Eternity Service
 */
@Controller
@RequestMapping("/services")
@Slf4j
public class EternityServiceController {

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
    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.GET)
    public @ResponseBody List<List<FigureVectorDTO>> queryBoard(@PathVariable(name = "boardId") String boardId) throws Exception {
        try(InputStream inputStream = new FileInputStream("/media/szergej/abd5d58a-ed2e-4bd0-82a3-cb5ea759edc7/workspace/MyProjects/eternity/data/data201805311518.json")){
            GoalDTO goalDTO = objectMapper.readValue(IOUtils.toString(inputStream,"UTF8"),GoalDTO.class);
            return boardParser.parse(goalDTO.getBoardTxt());
        }
    }




}
