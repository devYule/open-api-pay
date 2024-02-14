package my.test.kakaopaysketchbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayReadyDto {
    private String requestUrl;
//    private LinkedMultiValueMap<String, String> params;

    // for getReady
    private PayReadyBodyInfo payReadyBodyInfo;
}
