package my.test.kakaopaysketchbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import my.test.kakaopaysketchbook.model.*;
import my.test.kakaopaysketchbook.properties.ApiPayProperty;
import my.test.kakaopaysketchbook.requester.KakaoPayRequester;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;


@RequiredArgsConstructor
@Service
public class KakaoPayService {

    private final ObjectMapper om;
    private final KakaoPayRequester kakaoPayRequester;
    private final ApiPayProperty payProperty;
    private final EntityManager em;

    /**
     * 준비과정 -> tid 발급, persist, 리다이렉트 url 발급 (내가 제공한 redirect 에다가 pgToken 까지 붙혀서 리턴해줌)
     * tid 는 persist, 리다이렉트 url 은 프론트로 전달 (프론트는 해당 url 로 다시 요청할것임.)
     * 이 리다이렉트 url 은 approve (승인), fail (실패), cancel (취소) 3종류로 구분되어 제공됨.
     * 실제 카카오측 요청은 KakaoPayRequester 에 코드 존재.
     * 해당 서비스는 요청에 필요한 데이터 세팅 + 요청 위임 의 역할.
     */
    @Transactional
    public PayReadyResponseDto getRedirectUrl(PayInfoDto payInfoDto) throws JsonProcessingException {
        // TODO Security 에서 필요 정보 가져오기
        // TODO DAO 접근시 @Transactional (JPA)



        String name = "user_name"; // security 에서 유저이름을 가져왔다고 가정
        Long id = 1L; // security 에서 pk 값을 가져왔다고 가정


        /*
        헤더 세팅
         */
        // TODO 이 부분도 application.yaml 에 추가하자.
        // TODO "Authorization" 도 application.yaml 에 추가하자.
        String auth = "SECRET_KEY " + (payProperty.getSecretKey().trim()); // Authorization 키에 해당하는 값 (헤더)
        /*
        요청 body 세팅
         */
        PayReadyDto payReadyDto = kakaoPayRequester.getReadyRequest(id, payInfoDto);

        // 요청 보내기
        RestClient restClient = RestClient.builder()
                .baseUrl(payReadyDto.getRequestUrl())
                .build();

        String response = restClient.post()
                .header("Authorization", auth)
                .body(payReadyDto.getPayReadyBodyInfo())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        PayReadyResponseDto payReadyResponseDto = om.readValue(response, PayReadyResponseDto.class);
        String tid = payReadyResponseDto.getTid();
        Member member = em.find(Member.class, 1);
        member.setTid(tid);
        // TODO tid 를 DAO 를 통해 어딘가 저장해두자.

        return payReadyResponseDto;
    }

    /**
     * success 일 경우 요청되는 url
     * id 로 해당 멤버의 tid 를 가져올 수 있음 (DB)
     * pgToken 과 id 를 통해 카카오측에 결제정보를 전달, 승인처리를 해줌.
     */
    public PayApproveResponseDto readySuccess(Long id, String pgToken) throws JsonProcessingException {
        // TODO Security 관련 작업

        // TODO 위에서 저장해둔 tid 를 DAO 에서부터 가져오자
        String tid = em.find(Member.class, 1).getTid();
        String auth = "SECRET_KEY " + (payProperty.getSecretKey().trim()); // TODO 이 부분도 application.yaml 에 추가하자.

        PayApproveDto approveRequest = kakaoPayRequester.getApproveRequest(tid, id, pgToken);
        String requestUrl = approveRequest.getRequestUrl();

        RestClient restClient = RestClient.builder()
                .baseUrl(requestUrl)
                .build();

        // TODO "Authorization" 도 application.yaml 에 추가하자.
        String response = restClient.post()
                .header("Authorization", auth)
                .body(approveRequest.getPayApproveBodyInfo())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        return om.readValue(response, PayApproveResponseDto.class);
    }
}
