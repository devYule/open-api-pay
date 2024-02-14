package my.test.kakaopaysketchbook.model;

import lombok.Getter;

/**
 * 결제정보 DTO
 */
@Getter
public class PayInfoDto {
    private Integer price;
    private String itemName;
}
