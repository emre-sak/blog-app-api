package com.springboot.blog.service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.dto.requests.CreatePostRequest;
import com.springboot.blog.dto.requests.UpdatePostRequest;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostRequest postRequest);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long id);
    PostDto updatePost(UpdatePostRequest postRequest, Long id);
    void deletePostById(Long id);
    List<PostDto> getPostByCategory(Long categoryId);
    List<PostDto> searchPosts(String query);
}
