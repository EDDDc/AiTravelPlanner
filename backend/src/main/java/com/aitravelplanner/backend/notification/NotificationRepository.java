package com.aitravelplanner.backend.notification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findByPlanIdAndStatus(UUID planId, NotificationStatus status);

  List<Notification> findByStatusAndTriggerTimeBefore(
      NotificationStatus status, OffsetDateTime triggerTime);
}
