package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService
{
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private PostRepository postRepo;

    @Override
    public CommentDto saveComment(long postId, CommentDto commentDto)
    {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post with Id : " + postId + " Not Found To Comment")
        );
//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
//
//        comment.setPost(post);
        Comment comment = new Comment(commentDto.getCmt_id(),commentDto.getName(),commentDto.getEmail(),commentDto.getBody(),post);
        Comment savedComment = commentRepo.save(comment);
        CommentDto savedCommentDto = mapToCommentDto(savedComment);
        return savedCommentDto;
    }

    @Override
    public String deleteCommentById(long commentId)
    {
        commentRepo.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment With Id : "+commentId+" cannot be Deleted -> Id Not Found")
        );
        commentRepo.deleteById(commentId);
        return "Comment With Id: "+commentId+" is Deleted";
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId)
    {
        List<Comment> commentList = commentRepo.findByPostId(postId);
        List<CommentDto> commentDtoList = commentList.stream().map(this::mapToCommentDto).toList();
        return commentDtoList;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto)
    {
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment Not Found : " + commentId + " Update Failed")
        );
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
//        Comment updatedComment = commentRepo.save(comment);
//        CommentDto updatedCommentDto = mapToCommentDto(updatedComment);
//        return updatedCommentDto;
        return mapToCommentDto(commentRepo.save(comment));
    }

    CommentDto mapToCommentDto(Comment comment)
    {
//        CommentDto commentDto = new CommentDto();
//        commentDto.setCmt_id(comment.getCmt_id());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
//        return commentDto;
        return new CommentDto(comment.getCmt_id(),comment.getName(),comment.getEmail(),comment.getBody());
    }

}
