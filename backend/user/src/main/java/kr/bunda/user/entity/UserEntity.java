package kr.bunda.user.entity;

import kr.bunda.user.type.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "user", indexes = {
        @Index(name = "index_user_id", columnList = "id"),
        @Index(name = "index_user_email", columnList = "email"),
        @Index(name = "index_user_phoneNumber", columnList = "phoneNumber")
})
@NoArgsConstructor
@Setter
@Getter
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private UserStatus status;

    @Column(length = 100)
    private Boolean marketing;

    @Column
    private LocalDate birth;

    @Column
    private Instant loginHistory;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthEntity auth;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshTokenEntity token;
}
