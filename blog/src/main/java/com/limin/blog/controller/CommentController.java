package com.limin.blog.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.limin.blog.dto.CommentAndArticle;
import com.limin.blog.entity.Comment;
import com.limin.blog.entity.User;
import com.limin.blog.service.CommentService;
import com.limin.blog.util.PageResult;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @RequestMapping("/add")
    @ResponseBody
    public String add(Comment comment) {
        commentService.add(comment);
        return "success";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Long commentId) {
        commentService.deleteCascadeById(commentId);
        return "success";
    }

    @RequestMapping("/manage/list/{option}")
    public ModelAndView list(
            @PathVariable(name = "option", required = true) String option,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpSession session) {
        ModelAndView mv = new ModelAndView("/manage/comment/list");
        mv.addObject("option", "commentManage");
        User user = (User) session.getAttribute("loginUser");

        PageResult<CommentAndArticle> pageResult = null;
        if ("my".equals(option)) {
            mv.addObject("commentOption", "my");
            pageResult = commentService.findPageCommentAndArticleMyPublish(user.getId(), pageNum, pageSize);
        } else if ("other".equals(option)) {
            mv.addObject("commentOption", "other");
            pageResult = commentService.findPageCommentAndArticleMyArticle(user.getId(), pageNum, pageSize);
        }
        mv.addObject("pageResult", pageResult);
        return mv;
    }
}
