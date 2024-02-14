package my.test.kakaopaysketchbook.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.pay.kakao")
@Component
public class ApiPayProperty {

    private String cid;
    private String secretKey;
    private String headerKey;
    private String headerValue;


}
