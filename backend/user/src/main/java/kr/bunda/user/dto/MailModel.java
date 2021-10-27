package kr.bunda.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class MailModel {
    @Builder
    @Getter
    @Setter
    @ToString
    public static class Send {
        private String to;
        private String toName;
        private String from;
        private String fromName;
        private String subject;
        private String contents;
    }
}
