package circulo.server.global.apiPayload.code.status;

import circulo.server.global.apiPayload.code.BaseErrorCode;
import circulo.server.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "Server error, please contact the administrator."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","Invalid request."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","Authentication required."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "Forbidden request."),

    // Auth 관련 에러
    INVALID_USER_ID_FORMAT(HttpStatus.BAD_REQUEST, "AUTH400", "Invalid한 유저 ID 형식 입니다."),
    UNSUPPORTED_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "AUTH401", "지원하지 않는 소셜 로그인 타입입니다."),
    MISSING_AUTHORITY(HttpStatus.FORBIDDEN, "AUTH403", "토큰에 포함된 인증 정보가 부족합니다."),

    // Token 관련 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN402", "토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN403", "refresh 토큰이 유효하지 않거나 존재하지 않습니다."),
    NULL_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN404", "토큰이 NULL 값입니다."),
    TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "TOKEN500", "토큰 처리 중 에러가 발생했습니다."),

    // User 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "사용자를 찾을 수 없습니다."),

    // PackagingRequest 관련 에러
    PACKAGING_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "PR404", "포장재 요청을 찾을 수 없습니다."),

    // PackageSubmission 관련 에러
    PACKAGE_SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "PS404", "포장재 전달 신청을 찾을 수 없습니다."),

    // Delivery 관련 에러
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "D404", "배달 객체를 찾을 수 없습니다."),
    NO_PENDING_DELIVERIES_FOUND(HttpStatus.NOT_FOUND, "D404", "PENDING 상태의 배달 객체를 찾을 수 없습니다."),

    // Vertex AI 관련 에러
    VERTEX_AI_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI500", "Vertex AI 호출을 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
