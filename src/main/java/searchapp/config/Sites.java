package searchapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import searchapp.entity.Site;

import java.util.ArrayList;

@Component
@Setter
@Getter
@ConfigurationProperties (prefix = "config")
public class Sites {
    private ArrayList<Site> sitesList;
}
