package my.test.kakaopaysketchbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayApproveDto {
    private String requestUrl;
//    private LinkedMultiValueMap<String, String> params;

    // for Approve
    private PayApproveBodyInfo payApproveBodyInfo;
}
