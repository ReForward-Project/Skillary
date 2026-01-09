package com.example.springskillaryback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "contents")
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Content {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte contentId;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private String body;

	@OneToOne
	private Post post;

	@ManyToOne
	private Creator creator;
}