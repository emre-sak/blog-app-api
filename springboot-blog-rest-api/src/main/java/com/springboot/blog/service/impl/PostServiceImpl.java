package com.springboot.blog.service.impl;

import com.springboot.blog.dto.requests.CreatePostRequest;
import com.springboot.blog.dto.requests.UpdatePostRequest;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.mappers.ModelMapperService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapperService modelMapper;

    @Override
    public PostDto createPost(CreatePostRequest postRequest) {

        Category category = categoryRepository.findById(postRequest.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", postRequest.getCategoryId())
        );

        Post post = modelMapper.forRequest().map(postRequest, Post.class);
        post.setCategory(category);

        Post newPost = postRepository.save(post);

        PostDto postResponse = modelMapper.forResponse().map(newPost, PostDto.class);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> contents =  listOfPosts
                .stream()
                .map(content -> modelMapper.forResponse().map(content, PostDto.class)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return modelMapper.forResponse().map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(UpdatePostRequest postRequest, Long id) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id));

        Category category = categoryRepository.findById(postRequest.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", postRequest.getCategoryId())
        );

        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setContent(postRequest.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);

        return modelMapper.forResponse().map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId)
        );

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map(post -> modelMapper.forResponse().map(post, PostDto.class)).toList();
    }

    @Override
    public List<PostDto> searchPosts(String query) {

        List<Post> posts = postRepository.searchPosts(query);

        List<PostDto> postDtos = posts
                .stream()
                .map((post) -> modelMapper.forResponse().map(post, PostDto.class)).toList();

        return postDtos;
    }

}
