package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.dto.requests.CreateCommentRequest;
import com.springboot.blog.dto.requests.UpdateCommentRequest;

import java.util.List;

public interface CommnetService {
    CommentDto createComment(Long postId, CreateCommentRequest commentRequest);
    List<CommentDto> getCommentsByPostId(Long postId);
    CommentDto getCommentById(Long postId, Long commentId);
    CommentDto updateComment(Long postId, Long commentId, UpdateCommentRequest commentRequest);
    void deleteComment(Long postId, Long commentId);
}
