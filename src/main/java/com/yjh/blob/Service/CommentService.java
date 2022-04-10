package com.yjh.blob.Service;


import com.yjh.blob.po.Comment;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogid);

    Comment saveComment(Comment comment);
}
