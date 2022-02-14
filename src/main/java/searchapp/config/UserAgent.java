package searchapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserAgent {
    @Getter
    private static String uaname;
    @Getter
    private static String uareferrer;
}
