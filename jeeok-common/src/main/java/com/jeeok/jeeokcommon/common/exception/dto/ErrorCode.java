package com.jeeok.jeeokcommon.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "E001", "error.invalid.input.value"), // Bad Request
    INVALID_TYPE_VALUE(400, "E002", "error.invalid.type.value"), // Bad Request
    ENTITY_NOT_FOUND(400, "E003", "error.entity.not.found"), // Bad Request
    UNAUTHORIZED(401, "E004", "error.unauthorized"), // The request requires an user authentication
    JWT_EXPIRED(401, "E005", "error.unauthorized"), // The request requires an user authentication
    ACCESS_DENIED(403, "E006", "error.access.denied"), // Forbidden, Access is Denied
    NOT_FOUND(404, "E010", "error.page.not.found"), // Not found
    METHOD_NOT_ALLOWED(405, "E011", "error.method.not.allowed"), // 요청 방법이 서버에 의해 알려졌으나, 사용 불가능한 상태
    REQUIRE_USER_JOIN(412, "E012", "error.user.not.exists"), // Server Error
    UNPROCESSABLE_ENTITY(422, "E020", "error.unprocessable.entity"), // Unprocessable Entity
    INTERNAL_SERVER_ERROR(500, "E999", "error.internal.server"), // Server Error

    //business error code
    BUSINESS_CUSTOM_MESSAGE(400, "B001", ""), // 사용자 정의 메시지를 넘기는 business exception
    DUPLICATE_INPUT_INVALID(400, "B002", "error.duplicate.input.value"), // 중복된 값을 입력하였습니다
    DB_CONSTRAINT_DELETE(400, "B003", "error.duplicate.input.value"); // 참조하는 데이터가 있어서 삭제할 수 없습니다

    private final int status;
    private final String code;
    private final String message;
}
