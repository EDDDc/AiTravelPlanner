package com.aitravelplanner.backend.notification;

import com.aitravelplanner.backend.common.model.BaseEntity;
import com.aitravelplanner.backend.plan.TravelPlan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "plan_id", nullable = false)
  private TravelPlan plan;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 30)
  private NotificationType type = NotificationType.REMINDER;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false, length = 20)
  private NotificationChannel channel = NotificationChannel.IN_APP;

  @Column(name = "trigger_time")
  private OffsetDateTime triggerTime;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "payload", columnDefinition = "jsonb")
  private Map<String, Object> payload;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private NotificationStatus status = NotificationStatus.PENDING;
}
