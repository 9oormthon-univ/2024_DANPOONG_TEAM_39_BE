package com.example._2024_danpoong_team_39_be.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findById(Long id);

    List<Notification> findByMemberId(Long memberId);

    Object findByMemberIdOrderByCreatedDateDesc(Long userId);
}
