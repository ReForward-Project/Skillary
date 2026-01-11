package com.example.springskillaryback.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private byte commentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private Comment parent;

	// 자기 참조: 자식 댓글들 (선택 사항)
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<Comment> children = new ArrayList<>();

	@Column(nullable = false)
	private int like;

	@ManyToOne
	private Post post;

	@ManyToOne
	private User user;
}