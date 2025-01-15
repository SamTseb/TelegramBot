package org.joyapi.mapper;

import org.joyapi.model.User;
import org.mapstruct.Mapper;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface UserMapper {

    User toUser(org.telegram.telegrambots.meta.api.objects.User user);

    default User extractUser(Message message){
        User user = toUser(message.getFrom());
        user.setJoinAt(LocalDateTime.ofInstant(
                                                Instant.ofEpochSecond(message.getDate()),
                                                ZoneId.systemDefault()
                                                )
                        );
        user.setChatID(message.getChatId());
        user.setProhibitedTags("");
        return user;
    }
}
