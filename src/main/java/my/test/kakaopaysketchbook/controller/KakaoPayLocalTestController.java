package my.test.kakaopaysketchbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 테스트용, 필요한 url 로 리다이렉트용.
 */
@Controller
@RequestMapping("/payment")
@Slf4j
public class KakaoPayLocalTestController {

    @GetMapping("/success/{code}")
    private String testSucceed(@PathVariable Integer code,
                               @RequestParam("pg_token") String pgToken) {
        log.debug("testSucceed pgToken = {}", pgToken);
        return "redirect:/api/pay/kakao/success/" + code + "?pg_token=" + pgToken;
    }

}
