package com.aitravelplanner.backend.plan;

import com.aitravelplanner.backend.common.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "day_plans")
public class DayPlan extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "plan_id", nullable = false)
  private TravelPlan plan;

  @Column(name = "day_index", nullable = false)
  private Integer dayIndex;

  @Column(name = "plan_date", nullable = false)
  private LocalDate date;

  @Column(name = "summary", length = 255)
  private String summary;

  @Column(name = "notes", columnDefinition = "text")
  private String notes;

  @OneToMany(
      mappedBy = "dayPlan",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private Set<Activity> activities = new HashSet<>();
}
