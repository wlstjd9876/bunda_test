package kr.bunda.user.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "token", indexes = {
        @Index(name = "index_token_id", columnList = "id"),
        @Index(name = "index_token_signature", columnList = "signature")})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String signature;

    @JoinColumn(name = "user_id")
    @OneToOne
    private UserEntity user;

    @Column(length = 1000, nullable = false)
    private String token;

    @Column(length = 15, nullable = false)
    private String ipAddress;

}
