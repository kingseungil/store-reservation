package com.zb.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // auth
    UNMATCHED_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ALREADY_EXISTED_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다."),
    NOT_SUPPORTED_USER_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 유저 타입입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_ACTIVATED_USER(HttpStatus.BAD_REQUEST, "활성화되지 않은 유저입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    EMPTY_TOKEN(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),

    // store
    ALREADY_EXISTED_STORE(HttpStatus.BAD_REQUEST, "이미 존재하는 상점입니다."),
    NOT_EXISTED_STORE(HttpStatus.NOT_FOUND, "존재하지 않는 상점입니다."),
    NOT_OWNER_STORE(HttpStatus.BAD_REQUEST, "상점의 소유자가 아닙니다."),

    // reservation
    NOT_EXISTED_RESERVATION(HttpStatus.NOT_FOUND, "존재하지 않는 예약입니다."),
    NOT_RESERVATION_OWNER(HttpStatus.BAD_REQUEST, "예약의 소유자가 아닙니다."),
    ALREADY_EXISTED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 존재하는 예약입니다."),
    CANT_CHANGE_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "예약 상태를 변경할 수 없습니다."),
    NOT_ARRIVE_TIME(HttpStatus.BAD_REQUEST, "도착 시간이 아닙니다. 10분 전부터 진행할 수 있습니다."),

    // review
    CANNOT_WRITE_REVIEW(HttpStatus.BAD_REQUEST, "리뷰를 작성할 수 없습니다."),
    ALREADY_WRITTEN_REVIEW(HttpStatus.BAD_REQUEST, "이미 작성한 리뷰가 있습니다."),
    NOT_EXISTED_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    NOT_REVIEW_OWNER(HttpStatus.BAD_REQUEST, "리뷰의 소유자가 아닙니다.");

    private final HttpStatus status;
    private final String message;
}
