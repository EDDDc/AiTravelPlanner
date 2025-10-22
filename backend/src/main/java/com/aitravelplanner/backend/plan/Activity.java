package com.aitravelplanner.backend.plan;

import com.aitravelplanner.backend.common.model.BaseEntity;
import com.aitravelplanner.backend.common.model.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "activities")
public class Activity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "day_plan_id", nullable = false)
  private DayPlan dayPlan;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 30)
  private ActivityType type = ActivityType.OTHER;

  @Column(name = "name", nullable = false, length = 180)
  private String name;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  @Embedded
  private Location location;

  @Column(name = "cost_estimate", precision = 12, scale = 2)
  private BigDecimal costEstimate;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "metadata", columnDefinition = "jsonb")
  private Map<String, Object> metadata;
}
