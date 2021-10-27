package kr.bunda.user.entity;

import kr.bunda.user.type.TokenUse;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "mail_token", indexes = {
        @Index(name = "index_mail_token_id", columnList = "id"),
        @Index(name = "index_mail_token_email", columnList = "email"),
        @Index(name = "index_mail_token_createdTime", columnList = "createdTime")})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MailTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String token;

    @Column(length = 50, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TokenUse tokenUse;
}
