package kr.bunda.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

public class TokenModel {


    @ApiModel(value = "refresh token login")
    @Getter
    @Setter
    public static class LOGIN {
        @ApiModelProperty(value = "refresh token", required = true)
        private String refreshToken;
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;
        private String accessExpires;
        private String refreshExpires;
    }


}
