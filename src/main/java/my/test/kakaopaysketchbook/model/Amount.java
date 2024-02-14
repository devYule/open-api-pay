package my.test.kakaopaysketchbook.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 공식 문서에서 필요한 데이터가 있다면 추가 가능.
 */
@Getter
@Setter
public class Amount {

    private int total; // 총 결제 금액
    private int tax_free; // 비과세 금액
    private int tax; // 부가세 금액

}
