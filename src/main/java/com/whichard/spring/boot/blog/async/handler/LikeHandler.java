package com.whichard.spring.boot.blog.async.handler;

import com.whichard.spring.boot.blog.async.EventHandler;
import com.whichard.spring.boot.blog.async.EventModel;
import com.whichard.spring.boot.blog.async.EventType;
import com.whichard.spring.boot.blog.domain.Message;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.service.BlogService;
import com.whichard.spring.boot.blog.service.MessageService;
import com.whichard.spring.boot.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author wq
 * @date 2019/5/10
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User from = userService.getUserById((long)model.getActorId());
        int toId = 1, fromId = 2;
        try {
            toId = blogService.getBlogById((long)model.getEntityId()).getUser().getId().intValue();
            fromId = from.getId().intValue();
            if(fromId == toId) return;
        //被赞blog作者的ID
        message.setToId(toId);
        message.setContent("用户" + from.getUsername() +
                " 赞了你的博客《" + blogService.getBlogById((long)model.getEntityId()).getTitle() + "》");
        // SYSTEM ACCOUNT
        //message.setFromId(3);
        message.setFromId(fromId);

        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        } catch (Exception e) {
            System.out.println("Error likeHandler : " + e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
