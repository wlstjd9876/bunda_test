package kr.bunda.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.bunda.user.entity.UserEntity;
import kr.bunda.user.type.Auth;
import kr.bunda.user.type.UserStatus;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.time.LocalDate;

public class UserModel {
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhoneEmailToken {

        @NotBlank
        @Email
        private String email;

        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        private String phoneNumber;

        private String token;

    }

    @ApiModel(value = "email model")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OnlyEmail {

        @NotBlank
        @Email
        @ApiModelProperty(value = "email", required = true)
        private String email;

    }

    @ApiModel(value = "email auth model")
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmailToken {

        @NotBlank
        @Email
        @ApiModelProperty(value = "email", required = true)
        private String email;

        @ApiModelProperty(value = "token")
        private String token;

    }


    @ApiModel
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignUp {

        @ApiModelProperty(required = true)
        @NotBlank
        @Email
        private String email;

        @ApiModelProperty
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~\\\\])(?=.*[0-9])[0-9A-Za-z!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~\\\\]{8,16}$")
        private String password;

        @ApiModelProperty
        @Pattern(regexp = "([a-zA-Z]{2,10})|([가-힣]{2,10})")
        private String name;

        @ApiModelProperty
        private String address;

        @ApiModelProperty
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        @JsonProperty(value = "phone_number")
        private String phoneNumber;

        @ApiModelProperty
        @Pattern(regexp = "^((19|20)\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
        private String birth;

        @ApiModelProperty
        private boolean marketing;
    }

    @ApiModel
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Login {
        @ApiModelProperty(value = "email", example = "xxx@naver.com", required = true)
        @NotBlank
        @Email
        private String email;

        @ApiModelProperty(value = "password", required = true)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~\\\\])(?=.*[0-9])[0-9A-Za-z!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~\\\\]{8,16}$")
        private String password;
    }

    @ApiModel
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OnlyPhone {
        @ApiModelProperty
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        @JsonProperty(value = "phone_number")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhoneToken {

        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
        private String phoneNumber;

        @NotEmpty
        private String token;

    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class UserDto {

        private Long id;

        private String email;

        @JsonIgnore
        private String password;

        private String name;

        private String address;

        private String phoneNumber;

        private LocalDate birth;

        private UserStatus status;

        private Boolean marketing;

        private Instant loginHistory;

        private Instant createdTime;

        private Instant modifyTime;

        private Auth auth;

        public static UserDto of(UserEntity entity) {
            return UserDto.builder()
                    .address(entity.getAddress())
                    .birth(entity.getBirth())
                    .createdTime(entity.getCreatedTime())
                    .email(entity.getEmail())
                    .id(entity.getId())
                    .loginHistory(entity.getLoginHistory())
                    .marketing(entity.getMarketing())
                    .modifyTime(entity.getModifyTime())
                    .name(entity.getName())
                    .phoneNumber(entity.getPhoneNumber())
                    .status(entity.getStatus())
                    .auth(entity.getAuth().getAuth())
                    .password(entity.getPassword())
                    .build();
        }
    }

}
