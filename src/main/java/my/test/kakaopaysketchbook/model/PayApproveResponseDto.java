package my.test.kakaopaysketchbook.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 공식 문서에서 필요한 데이터가 있다면 추가 가능.
 */
@Getter
@Setter
public class PayApproveResponseDto {

    private Amount amount; // 결제 금액 정보
    private String item_name; // 상품 명
    private String created_at; // 결제 요청 시간
    private String approved_at; // 결제 승인 시간
}
