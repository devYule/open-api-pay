package my.test.kakaopaysketchbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import my.test.kakaopaysketchbook.model.*;
import my.test.kakaopaysketchbook.properties.ApiPayProperty;
import my.test.kakaopaysketchbook.properties.KakaoPayRequestUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class KakaoPayService {

    private final ObjectMapper om;
    private final KakaoPayRequestUtils kakaoPayRequestUtils;
    private final ApiPayProperty payProperty;

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
        PayReadyDto payReadyDto = kakaoPayRequestUtils.getReadyRequest(id, payInfoDto);

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
        // TODO tid 를 DAO 를 통해 어딘가 저장해두자.

        return payReadyResponseDto;
    }

    public PayApproveResponseDto readySuccess(Long id, String pgToken) throws JsonProcessingException {
        // TODO Security 관련 작업

        // TODO 위에서 저장해둔 tid 를 DAO 에서부터 가져오자
        String tid = "가져온tid";
        String auth = "SECRET_KEY " + (payProperty.getSecretKey().trim()); // TODO 이 부분도 application.yaml 에 추가하자.

        PayApproveDto approveRequest = kakaoPayRequestUtils.getApproveRequest(tid, id, pgToken);
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
