package com.eureka.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "beacons")
@AllArgsConstructor
@IdClass(BeaconId.class)
public class Beacon {
	@Id
	@Column(name = "uuid")
	private String uuid;
	@Id
	@Column(name = "major")
	private String major;
	@Id
	@Column(name = "minor")
	private String minor;
	
	@ManyToOne
    @JoinColumn(name="store_no", nullable=false)
    private Store store;
	
	public Beacon() {}
	
	
}
