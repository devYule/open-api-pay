package my.test.kakaopaysketchbook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공식 문서에서 필요한 데이터가 있다면 추가 가능.
 * tid, next_redirect_pc_url 은 필수 (pc or 기타 os)
 */
@Getter
@Setter
public class PayReadyResponseDto {
    private String tid;
    private String next_redirect_pc_url;
    private String created_at;
}
