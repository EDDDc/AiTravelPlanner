package com.aitravelplanner.backend.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Location {

  @Column(name = "location_lat")
  private Double latitude;

  @Column(name = "location_lng")
  private Double longitude;

  @Column(name = "location_address")
  private String address;
}
