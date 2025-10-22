package com.aitravelplanner.backend.plan;

import com.aitravelplanner.backend.common.model.BaseEntity;
import com.aitravelplanner.backend.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "travel_plans")
public class TravelPlan extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "title", nullable = false, length = 160)
  private String title;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "destinations", columnDefinition = "jsonb")
  private List<String> destinations = new ArrayList<>();

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "budget_total", precision = 12, scale = 2)
  private BigDecimal budgetTotal;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "preferences", columnDefinition = "jsonb")
  private Map<String, Object> preferences;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private PlanStatus status = PlanStatus.DRAFT;

  @OneToMany(
      mappedBy = "plan",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private Set<DayPlan> days = new HashSet<>();

}
