package my.test.kakaopaysketchbook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.test.kakaopaysketchbook.model.PayApproveResponseDto;
import my.test.kakaopaysketchbook.model.PayReadyResponseDto;
import my.test.kakaopaysketchbook.model.PayInfoDto;
import my.test.kakaopaysketchbook.service.KakaoPayService;
import org.springframework.web.bind.annotation.*;


/**
 * 이 예제에서는 단순히 호출하는 로직만 작성.
 * 데이터 등의 필요한 후작업은 로직마다 추가하여 작성 필수
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/pay/kakao")
public class KakaoPayController {
    private final KakaoPayService kakaopayService;

    /**
     * 결제 준비
     * redirect url 받기
     * 상품명과 가격을 같이 보내주어야 함. (PayInfoDto)
     */
    @GetMapping("/ready")
    public PayReadyResponseDto doReadyRequest(@RequestBody PayInfoDto payInfoDto) {
        try {
            return kakaopayService.getRedirectUrl(payInfoDto);
        } catch (JsonProcessingException e) {
            log.debug("KakaoPayController.doReadyRequest()  ex", e);
            throw new RuntimeException();
        }
    }

    /**
     * ready 성공시
     */
    @GetMapping("/success/{id}")
    public PayApproveResponseDto readySuccess(@PathVariable Long id,
                                              @RequestParam("pg_token") String pgToken) {
        log.debug("readySuccess pgToken = {}", pgToken);
        try{
            return kakaopayService.readySuccess(id, pgToken);
        }catch (JsonProcessingException e) {
            log.debug("KakaoPayController.readySuccess()  ex", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * ready 실패시
     */
    @GetMapping("/fail")
    public void readyFail() {
        log.debug("KakaoPayController.readyFail()  !!!");
        throw new RuntimeException();
    }

}
