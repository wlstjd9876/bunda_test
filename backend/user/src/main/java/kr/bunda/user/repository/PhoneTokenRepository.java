package kr.bunda.user.repository;

import kr.bunda.user.entity.PhoneTokenEntity;
import kr.bunda.user.type.TokenUse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneTokenRepository extends JpaRepository<PhoneTokenEntity, Long> {

    Optional<PhoneTokenEntity> findByPhone(String phone);

    Optional<PhoneTokenEntity> findByPhoneAndTokenUse(String phone, TokenUse tokenUse);

    Long deleteByPhoneAndTokenUse(String phone, TokenUse tokenUse);

    void deleteByPhone(String phone);
}
