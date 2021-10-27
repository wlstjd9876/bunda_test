package kr.bunda.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class PhoneModel {
    @Builder
    @Getter
    @Setter
    @ToString
    public static class Send {
        private String to;
        private String subject;
        private String contents;
    }
}
