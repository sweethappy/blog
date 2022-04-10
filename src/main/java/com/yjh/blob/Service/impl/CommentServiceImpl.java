package com.yjh.blob.Service.impl;

import com.yjh.blob.Service.CommentService;
import com.yjh.blob.dao.CommentRepository;
import com.yjh.blob.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogid) {
        Sort sort = new Sort("createTime");
        List<Comment> byBlogIdAndParentCommentNot = commentRepository.findByBlogIdAndParentCommentNull(blogid, sort);
        return eachComment(byBlogIdAndParentCommentNot);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId!=-1){
            comment.setParentComment(commentRepository.findOne(parentCommentId));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        if(comment.getId()==null){
            System.out.println("id值为空"+comment.getId());
            return commentRepository.save(comment);
        }
        else {
            return commentRepository.save(comment);
        }

    }

    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for(Comment comment:comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments){
        for(Comment comment:comments){
            List<Comment> replys1 = comment.getReplyComment();
            for(Comment replys:replys1){
                recursively(replys);
            }
            comment.setReplyComment(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();

    private void recursively(Comment comment){
        tempReplys.add(comment);
        if(comment.getReplyComment().size()>0){
            List<Comment> replys = comment.getReplyComment();
            for(Comment reply:replys){
                tempReplys.add(reply);
                if(reply.getReplyComment().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
