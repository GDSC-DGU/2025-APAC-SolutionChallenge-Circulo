package circulo.server.domain.packagingRequest.converter;

import circulo.server.domain.packagingRequest.dto.response.PackagingRequestResponse;
import circulo.server.domain.packagingRequest.entity.PackagingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PackagingRequestConverter {

    public PackagingRequestResponse.PackageRequestSuccessResponse toPackageRequestSuccess(PackagingRequest packagingRequest) {
        return PackagingRequestResponse.PackageRequestSuccessResponse.builder()
                .id(packagingRequest.getId())
                .build();
    }

    public List<PackagingRequestResponse.PackagingRequestListItem> toPackageRequestList(List<PackagingRequest> packagingRequestList) {

        return packagingRequestList.stream()
                .map(r -> new PackagingRequestResponse.PackagingRequestListItem(
                        r.getId(),
                        r.getPackagingType().name(),
                        r.getQuantity(),
                        r.getLocation(),
                        "0m" // TODO: 거리 계산 필요시 여기에 적용
                ))
                .toList();
    }

    public PackagingRequestResponse.PackagingRequestDetailResponse toPackageRequestDetailResponse(PackagingRequest packagingRequest) {
        return PackagingRequestResponse.PackagingRequestDetailResponse.builder()
                .id(packagingRequest.getId())
                .quantity(packagingRequest.getQuantity())
                .location(packagingRequest.getLocation())
                .type(packagingRequest.getPackagingType().name())
                .distance("0m")
                .build();
    }

    public PackagingRequestResponse.PackagingRequestResponseDto toPackagingRequestResponseDto(PackagingRequest packagingRequest) {
        return PackagingRequestResponse.PackagingRequestResponseDto.builder()
                .requestId(packagingRequest.getId())
                .packagingType(packagingRequest.getPackagingType().name())
                .quantity(packagingRequest.getQuantity())
                .location(packagingRequest.getLocation())
                .status(packagingRequest.getStatus().name())
                .createdAt(packagingRequest.getCreatedAt())
                .build();
    }

}
