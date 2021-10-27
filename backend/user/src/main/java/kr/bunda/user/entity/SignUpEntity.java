package kr.bunda.user.entity;

import kr.bunda.user.type.SignUpProcess;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sign_up", indexes = {
        @Index(name = "index_sign_up_email", columnList = "email"),
        @Index(name = "index_sign_up_createdTime", columnList = "createdTime")
})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SignUpEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private SignUpProcess process;

}
