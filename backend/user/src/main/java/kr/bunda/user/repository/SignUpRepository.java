package kr.bunda.user.repository;

import kr.bunda.user.entity.SignUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface SignUpRepository extends JpaRepository<SignUpEntity, Long> {

    Optional<SignUpEntity> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteAllByCreatedTimeBefore(Instant createTime);
}
