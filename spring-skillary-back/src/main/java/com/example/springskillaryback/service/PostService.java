package com.example.springskillaryback.service;

import com.example.springskillaryback.common.dto.CommentRequestDto;
import com.example.springskillaryback.common.dto.PostRequestDto;
import com.example.springskillaryback.domain.Comment;
import com.example.springskillaryback.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
	Post retrivePost(byte postId);

	Page<Post> pagingPosts(Pageable pagable);

	Post createPost(PostRequestDto postRequestDto);

	Post updatePost(byte postId, PostRequestDto postRequestDto);

	void deletePost(byte postId, byte creatorId);

	Comment addComment(CommentRequestDto commentRequestDto);

	Comment updateComment(byte commentId, CommentRequestDto commentRequestDto);

	void deleteComment(byte commentId, CommentRequestDto commentRequestDto);
}
