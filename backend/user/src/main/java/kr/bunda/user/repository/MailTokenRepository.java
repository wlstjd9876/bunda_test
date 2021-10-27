package kr.bunda.user.repository;

import kr.bunda.user.entity.MailTokenEntity;
import kr.bunda.user.type.TokenUse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface MailTokenRepository extends JpaRepository<MailTokenEntity, Long> {

    Optional<MailTokenEntity> findByEmail(String email);

    Optional<MailTokenEntity> findByEmailAndTokenUse(String email, TokenUse tokenUse);

    void deleteByEmail(String email);

    Integer deleteByCreatedTimeBefore(Instant createdTime);
}
