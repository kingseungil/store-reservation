package com.zb.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),

    // auth
    UNMATCHED_PASSWORD("비밀번호가 일치하지 않습니다."),
    ALREADY_EXISTED_USER("이미 존재하는 유저네임입니다."),
    NOT_SUPPORTED_USER_TYPE("지원하지 않는 유저 타입입니다."),
    USER_NOT_FOUND("존재하지 않는 유저입니다."),
    NOT_ACTIVATED_USER("활성화되지 않은 유저입니다."),
    ACCESS_DENIED("접근 권한이 없습니다."),
    UNAUTHORIZED("인증되지 않은 사용자입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    EMPTY_TOKEN("토큰이 존재하지 않습니다."),

    // store
    ALREADY_EXISTED_STORE("이미 존재하는 상점입니다."),
    NOT_EXISTED_STORE("존재하지 않는 상점입니다."),
    NOT_OWNER_STORE("상점의 소유자가 아닙니다."),

    // reservation
    NOT_EXISTED_RESERVATION("존재하지 않는 예약입니다."),
    NOT_RESERVATION_OWNER("예약의 소유자가 아닙니다."),
    ALREADY_EXISTED_RESERVATION("이미 존재하는 예약입니다."),
    //    ALREADY_CANCELED_RESERVATION("이미 취소된 예약입니다."),
//    ALREADY_ARRIVED_RESERVATION("이미 완료된 예약입니다."),
//    ALREADY_REJECTED_RESERVATION("이미 거절된 예약입니다."),
    DONT_CHANGE_RESERVATION_STATUS("예약 상태를 변경할 수 없습니다."),
    NOT_ARRIVE_TIME("도착 시간이 아닙니다. 10분 전부터 진행할 수 있습니다.");

    private final String description;
}
