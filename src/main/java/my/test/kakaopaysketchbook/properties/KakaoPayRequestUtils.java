package my.test.kakaopaysketchbook.properties;

import lombok.RequiredArgsConstructor;
import my.test.kakaopaysketchbook.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

/**
 * 외부 API 와 통신을 하여, 요청에대한 응답을 받는 유틸.
 */
@Component
@RequiredArgsConstructor
public class KakaoPayRequestUtils {

    private final ApiPayProperty payProperty;

    public PayReadyDto getReadyRequest(Long id, PayInfoDto payInfoDto) {
        /**
         * partner_user_id, partner_order_id
         * 는 결제 승인 요청에서도 동일해야 한다.
         * 값은 자유지만, 주문번호와, 회원의 식별자값을 주는게 좋다.
         */
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String cid = payProperty.getCid(); // cid = 가맹점 번호, 테스트시에는 TC0ONETIME 로 고정
        String memberId = "" + id; // partner_user_id = 가맹점의 주문번호, 최대 100자
        String orderId = "order_id" + "id"; // partner_order_id = 가맹점의 회원 id, 최대 100자

        String itemName = payInfoDto.getItemName(); // item_name = 상품명, 최대 100자
        Integer quantity = 1; // quantity = 상품 수량
        String totalAmount = String.valueOf(payInfoDto.getPrice() * quantity); // total_amount = 상품의 총 결제 금액
        Integer taxFreeAmount = 0; // tax_free_amount = 상품 비과세 금액 (세금 금액인듯?)
        String successRedirectUrl = "http://localhost:8080/payment/success/" + id; // approval_url = 결제 준비 성공시 리다이렉트 url + / + id값
        String cancelRedirectUrl = "http://localhost:8080/payment/cancel"; // cancel_url = 결제 취소시 리다이렉트 url
        String failRedirectUrl = "http://localhost:8080/payment/fail"; // fail_url = 결제 실패시 리다이렉트 url

        String requestUrl = "https://open-api.kakaopay.com/online/v1/payment/ready"; // 요청 보낼 url (POST) - to 카카오

        // 파라미터 세팅
        PayReadyBodyInfo payReadyBodyInfo = new PayReadyBodyInfo(cid, memberId, orderId, itemName, quantity.toString(), totalAmount,
                taxFreeAmount.toString()
                , successRedirectUrl, cancelRedirectUrl, failRedirectUrl);

        params.add("cid", cid);
        params.add("partner_user_id", memberId);
        params.add("partner_order_id", orderId);
        params.add("item_name", itemName);
        params.add("quantity", quantity.toString());
        params.add("total_amount", totalAmount);
        params.add("tax_free_amount", taxFreeAmount.toString());
        params.add("approval_url", successRedirectUrl);
        params.add("cancel_url", cancelRedirectUrl);
        params.add("fail_url", failRedirectUrl);

        return new PayReadyDto(requestUrl, payReadyBodyInfo);
    }

    public PayApproveDto getApproveRequest(String tidParam, Long id, String pgToken) {

        String memberId = "" + id; // partner_user_id = 가맹점의 주문번호, 최대 100자
        String orderId = "order_id" + "id"; // partner_order_id = 가맹점의 회원 id, 최대 100자

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String cid = payProperty.getCid(); // cid = 가맹점 번호, 테스트시에는 TC0ONETIME 로 고정
        String tid = tidParam; // 결제 고유 번호, 20자

        String pg_token = pgToken;
        // pg_tokent = 결제승인 요청을 인증하는 토큰
        //사용자 결제 수단 선택 완료 시, approval_url로 redirection해줄 때 pg_token을 query string으로 전달

        /*
            description
            getReadyRequest 에서 받아온 redirect url 에 클라이언트가 접속하여 결제수단 선택을 완료하면,
            http://localhost:8080/payment/success 로 redirect 가 된다.
            (정확히는 http://localhost:8080/payment/success/{id} 이다. 그렇게 설정했다 내가.)
            이 부분에 &pg_token=토큰값 이 함께 오게 된다.
            이 쿼리파라미터를 뽑아서 실 결제때 사용해야 한다.
         */

        PayApproveBodyInfo payApproveBodyInfo = new PayApproveBodyInfo(cid, tid, orderId, memberId, pg_token);
        String requestUrl = "https://open-api.kakaopay.com/online/v1/payment/approve";
        params.add("cid", cid);
        params.add("tid", tid);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", memberId);
        params.add("pg_token", pgToken);

        return new PayApproveDto(requestUrl, payApproveBodyInfo);

    }
}
