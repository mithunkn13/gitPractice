package com.blog.controller;

import com.blog.entity.Post;
import com.blog.payload.PostDto;
import com.blog.service.PostService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController
{
    private PostService postService;
    PostController(PostService postService)
    {
        this.postService=postService;
    }

   // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> savePost(@Valid  @RequestBody PostDto postDto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            List<String> validationErrorMessageList = new ArrayList<>();
            for(FieldError fieldError : fieldErrorList)
            {
                validationErrorMessageList.add(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(validationErrorMessageList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto savedPostDto = postService.savePost(postDto);
        return new ResponseEntity<>(savedPostDto,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAllPostController(
           @RequestParam(name="pageNo",defaultValue = "0",required = false)int pageNo,
           @RequestParam(name="pageSize", defaultValue = "3",required = false)int pageSize,
           @RequestParam(name="sortBy",defaultValue = "id",required = false)String sortBy,
           @RequestParam(name="sortDir",defaultValue = "asc",required = false)String sortDir
    )
    {
        Page<Post> postPage = postService.getAllPost(pageNo,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(postPage,HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostController(
            @PathVariable(name="id" ,required = true)long id
    )
    {
        String deleteMessage = postService.deletePostById(id);
        return new ResponseEntity<>(deleteMessage,HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<PostDto> updatePostController(
            @RequestParam(name="id",required = true)long id,
            @RequestBody PostDto postDto
    )
    {
        PostDto updatePostDto = postService.updatePost(id,postDto);
        return  new ResponseEntity<>(updatePostDto,HttpStatus.OK);
    }
}
