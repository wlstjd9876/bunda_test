package kr.bunda.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseModel {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
}
