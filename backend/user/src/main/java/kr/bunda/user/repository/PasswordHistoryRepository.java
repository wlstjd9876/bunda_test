package kr.bunda.user.repository;

import kr.bunda.user.entity.PasswordHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistoryEntity, Long> {

}
