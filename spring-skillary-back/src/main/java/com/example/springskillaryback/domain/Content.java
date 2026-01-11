package com.example.springskillaryback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "contents")
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Content {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Byte contentId;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(length = 20, nullable = false)
	private CategoryEnum category;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder.Default
	@ManyToOne
	@JoinColumn(name = "plan_id")
	private SubscriptionPlan plan = null;

	@OneToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne
	private Creator creator;
}