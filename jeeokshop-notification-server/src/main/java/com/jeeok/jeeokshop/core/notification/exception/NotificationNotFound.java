package com.jeeok.jeeokshop.core.notification.exception;

import com.jeeok.jeeokshop.common.exception.EntityNotFoundException;
import com.jeeok.jeeokshop.common.exception.ErrorCode;

import java.util.List;

public class NotificationNotFound extends EntityNotFoundException {


    public static final String MESSAGE = "Notification Not Found";

    public NotificationNotFound(Long entityId) {
        super(MESSAGE, List.of(new ErrorCode("notification", "entity.notification", new Object[]{entityId})));
    }
}
