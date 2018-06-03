package fg.eternity.util;

import com.google.common.collect.Collections2;
import fg.eternity.bo.Config;
import fg.eternity.bo.Field;
import fg.eternity.bo.FigureVectorDTO;
import fg.eternity.validator.Validator;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardParser {

    private List<Field> fields;

    @Value("${fg.eternity.defaults}")
    private String defaults;

    @Autowired
    @Setter
    private Config config;

    @PostConstruct
    public void init() {
        fields = Arrays.asList(Field.generateAllFields(config));
    }

    public static final Pattern FIGURE_PARSER = Pattern.compile("(\\w\\d{1,2})\\((\\d{1,3})\\)\\<(.{4})\\>\\^(\\d)");

    public List<List<FigureVectorDTO>> parse(String boardStr) {
        Matcher matcher = FIGURE_PARSER.matcher(defaults+","+boardStr);
        Map<Integer, FigureVectorDTO> figureVectorMap = new TreeMap<>();
        while (matcher.find()) {
            String fieldTxt = matcher.group(1);
            List<Field> filteredFields = new ArrayList<>(Collections2.filter(fields, field1 -> field1 != null && field1.toStringg().equals(fieldTxt)));
            Validator.equals(1, filteredFields.size(), "No value exists for {}", fieldTxt);
            Integer fieldId = filteredFields.get(0).getPos();
            String figureCode = matcher.group(3);
            Integer rotation = Integer.valueOf(matcher.group(4));
            Integer figureId = Integer.valueOf(matcher.group(2));
            FigureVectorDTO figureVectorDTO = new FigureVectorDTO();
            figureVectorDTO.setFieldTxt(fieldTxt);
            figureVectorDTO.setId(figureId);
            figureVectorDTO.setRotation(transferRotation(rotation));
            figureVectorDTO.setValue(figureCode);
            figureVectorMap.put(fieldId, figureVectorDTO);
        }
        for (Field field:fields) {
            if(field!=null){
                figureVectorMap.computeIfAbsent(field.getPos(), id -> {
                    FigureVectorDTO figureVectorDTO = new FigureVectorDTO();
                    figureVectorDTO.setFieldTxt(field.toStringg());
                    figureVectorDTO.setId(0);
                    figureVectorDTO.setRotation(transferRotation(0));
                    figureVectorDTO.setValue("****");
                    return figureVectorDTO;
                });
            }
        }
        List<List<FigureVectorDTO>> ret = new ArrayList<>();
        figureVectorMap.forEach((id, figureVectorDTO) -> {
            if (id % 18 == 0) {
                ret.add(new ArrayList<>());
            }
            List<FigureVectorDTO> figureVectorDTOS = ret.get(ret.size() - 1);
            figureVectorDTOS.add(figureVectorDTO);
        });
        Collections.reverse(ret);
        return ret;
    }

    private String transferRotation(Integer i) {
        switch (i) {
            case 0:
                return "0";
            case 1:
                return "90";
            case 2:
                return "180";
            case 3:
                return "270";
        }
        return Validator.fail("Invalid value:{}", i);
    }


}
