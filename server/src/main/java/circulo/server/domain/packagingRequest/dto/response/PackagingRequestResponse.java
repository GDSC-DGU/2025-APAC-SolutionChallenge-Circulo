package circulo.server.domain.packagingRequest.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PackagingRequestResponse {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PackageRequestSuccessResponse {
        // 포장재 요청 글 id
        Long id;
    }

    @Getter
    @AllArgsConstructor
    public static class PackagingRequestListItem {
        private Long id;
        private String type;
        private Integer quantity;
        private String location;
        private String distance;  // 거리 계산 추후에 채우기
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PackagingRequestListResponse {
        private List<PackagingRequestListItem> results;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PackagingRequestDetailResponse {
        private Long id;
        private String type;
        private Integer quantity;
        private String location;
        private String distance;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PackagingRequestResponseDto {
        private Long requestId;
        private String packagingType;
        private Integer quantity;
        private String location;
        private String status;
        private LocalDateTime createdAt;
    }
}
