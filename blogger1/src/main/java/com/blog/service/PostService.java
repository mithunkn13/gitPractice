package com.blog.service;

import com.blog.payload.PostDto;
import com.blog.entity.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    PostDto savePost(PostDto postDto);

    Page<Post> getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);

    String deletePostById(long id);

    PostDto updatePost(long id, PostDto postDto);
}
