package kr.bunda.user.entity;

import kr.bunda.user.type.TokenUse;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phone_token", indexes = {
        @Index(name = "index_phone_token_id", columnList = "id"),
        @Index(name = "index_phone_token_phone", columnList = "phone")})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PhoneTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6, nullable = false)
    private String token;

    @Column(length = 15, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private TokenUse tokenUse;
}
