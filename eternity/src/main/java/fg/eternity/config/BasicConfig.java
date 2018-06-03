package fg.eternity.config;

import fg.eternity.util.BoardParser;
import fg.eternity.util.Utility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource({
        "classpath:fg/eternity/config/eternity.properties"
})
public class BasicConfig {

    @Bean
    public Map<Integer,String> figureMap(){
        return Utility.loadFigures();
    }

    @Bean(initMethod="init" )
    public BoardParser boardParser(){
        return new BoardParser();
    }


}
