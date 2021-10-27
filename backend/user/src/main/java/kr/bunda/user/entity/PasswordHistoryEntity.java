package kr.bunda.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "password_history", indexes = {@Index(name = "password_history_index", columnList = "modifiedTime")})
@Setter
@Getter
public class PasswordHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private UserEntity userEntity;

    @Column(length = 100, nullable = false)
    private String password;

    @Column
    private Instant modifiedTime;
}
