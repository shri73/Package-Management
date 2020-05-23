package com.example.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "admin")
public class Admin {
	
	@Id
	@GenericGenerator(name = "sequence_dep_id", strategy = "com.example.demo.model.StringPrefixedSequenceIdGenerator")
	@GeneratedValue(generator = "sequence_dep_id")  
    @Column(name = "employee_id")
    private String id;
	
	@Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
	@Length(max = 60)
    private String password;
    
    @Column(name = "name")
    @NotEmpty(message = "Please provide your name")
    private String name;
    
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role roles;
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "store_no", referencedColumnName = "store_no")
    private Store store;
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
        builder.append("{\"name\" :");
        builder.append(name);
        builder.append(", \"password\" :");
        builder.append(password);
        builder.append("}");
    return builder.toString();
		
	}

}
