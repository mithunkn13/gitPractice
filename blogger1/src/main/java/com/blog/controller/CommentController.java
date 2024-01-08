package com.blog.controller;

import com.blog.payload.CommentDto;
import com.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController
{
    @Autowired
    private CommentService commentService;
    @PostMapping
    public ResponseEntity<?> saveCommentController(
            @RequestParam(name="postId",required = true) long postId,
           @Valid @RequestBody(required = true) CommentDto commentDto,
            BindingResult bindingResult
    )
    {
        if(bindingResult.hasErrors())
        {
            List<String> validationErrorMessageList = new ArrayList<>();
            for( FieldError fieldError : bindingResult.getFieldErrors())
            {
                validationErrorMessageList.add(fieldError.getDefaultMessage());
            }
            return  new ResponseEntity<>(validationErrorMessageList,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDto savedCommentDto = commentService.saveComment(postId,commentDto);
        return new ResponseEntity<>(savedCommentDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentController(
            @PathVariable long commentId
    )
    {
        String deleteMessage = commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(deleteMessage,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByPostIdController(
            @RequestParam(name="postId") long postId
    )
    {
        List<CommentDto> commentsDtoList = commentService.getCommentsByPostId(postId);
        return  new ResponseEntity<>(commentsDtoList,HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable long commentId,
            @RequestBody CommentDto commentDto
    )
    {
        CommentDto updatedCommentDto = commentService.updateComment(commentId,commentDto);
        return new ResponseEntity<>(updatedCommentDto,HttpStatus.OK);
    }




}
