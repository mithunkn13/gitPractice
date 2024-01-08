package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService
{
    @Autowired
    private PostRepository postRepo;
    @Override
    public PostDto savePost(PostDto postDto) {
        Post post = mapToPost(postDto);
        Post savedPost = postRepo.save(post);
        PostDto savedPostDto = mapToPostDto(savedPost);
        return savedPostDto;
    }

    @Override
    public Page<Post> getAllPost(int pageNo, int pageSize, String sortBy, String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> pagePost = postRepo.findAll(pageRequest);
        return pagePost;
    }

    @Override
    public String deletePostById(long id)
    {
        Optional<Post> optional = postRepo.findById(id);
        optional.orElseThrow(
                ()->   new ResourceNotFoundException("Post with Id : "+id+"Not Found")
        );
        postRepo.deleteById(id);
        return "Post id deleted , id : "+id;
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Optional<Post> optional = postRepo.findById(id);
        Post post = optional.orElseThrow(
                () -> new ResourceNotFoundException("Post Not Found With Id : " + id)
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepo.save(post);
        PostDto updatedPostDto = mapToPostDto(updatedPost);
        return updatedPostDto;

    }

    Post mapToPost(PostDto postDto)
    {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    PostDto mapToPostDto(Post post)
    {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }
}
