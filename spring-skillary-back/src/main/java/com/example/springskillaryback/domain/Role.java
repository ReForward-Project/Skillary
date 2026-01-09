package com.example.springskillaryback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte roleId;

	@Enumerated(STRING)
	@Column(length = 50, nullable = false, unique = true)
	private RoleEnum role;
}