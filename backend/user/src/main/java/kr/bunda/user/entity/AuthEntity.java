package kr.bunda.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.bunda.user.type.Auth;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "auth", indexes = {@Index(name = "index_auth_user_id", unique = true, columnList = "user_id")})
public class AuthEntity extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Auth auth;

    @JoinColumn(name = "user_id")
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Override
    public String getAuthority() {
        return auth.name();
    }

}