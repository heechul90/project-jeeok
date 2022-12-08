package com.jeeok.jeeokshop.core.notification.exception;

import com.jeeok.jeeokshop.common.exception.EntityNotFoundException;

import java.util.List;

public class NotificationNotFound extends EntityNotFoundException {


    public static final String MESSAGE = "Notification Entity Not Found. id=";

    public NotificationNotFound(Long id) {
        super(MESSAGE + id);
    }
}
