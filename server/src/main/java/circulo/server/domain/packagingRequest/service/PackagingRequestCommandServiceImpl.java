package circulo.server.domain.packagingRequest.service;

import circulo.server.domain.packagingRequest.converter.PackagingRequestConverter;
import circulo.server.domain.packagingRequest.dto.request.PackagingRequestRequest;
import circulo.server.domain.packagingRequest.dto.response.PackagingRequestResponse;
import circulo.server.domain.packagingRequest.entity.PackagingRequest;
import circulo.server.domain.packagingRequest.entity.enums.PackagingRequestStatus;
import circulo.server.domain.packagingRequest.repository.PackagingRequestRepository;
import circulo.server.domain.user.entity.User;
import circulo.server.domain.user.repository.UserRepository;
import circulo.server.global.apiPayload.code.exception.custom.PackagingRequestException;
import circulo.server.global.apiPayload.code.exception.custom.UserException;
import circulo.server.global.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PackagingRequestCommandServiceImpl implements PackagingRequestCommandService {

    private final PackagingRequestRepository packagingRequestRepository;
    private final UserRepository userRepository;
    private final PackagingRequestConverter packagingRequestConverter;

    @Override
    public PackagingRequestResponse.PackageRequestSuccessResponse createPackagingRequest(Long userId, PackagingRequestRequest.CreatePackagingRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        PackagingRequest packagingRequest = PackagingRequest.builder()
                .user(user)
                .packagingType(request.getType())
                .quantity(request.getQuantity())
                .location(request.getLocation())
                .status(PackagingRequestStatus.IN_PROGRESS)
                .build();

        packagingRequestRepository.save(packagingRequest);

        return packagingRequestConverter.toPackageRequestSuccess(packagingRequest);
    }

    @Override
    public void completePackagingRequest(Long userId, Long packagingRequestId) {
        PackagingRequest packagingRequest = packagingRequestRepository.findById(packagingRequestId)
                .orElseThrow(() -> new PackagingRequestException(ErrorStatus.PACKAGING_REQUEST_NOT_FOUND));

        // 자신의 요청글이 아니면 권한 없음 오류 처리
        if (!packagingRequest.getUser().getId().equals(userId)) {
            throw new PackagingRequestException(ErrorStatus._FORBIDDEN);
        }

        packagingRequest.changeStatus(PackagingRequestStatus.COMPLETED);
    }

}
