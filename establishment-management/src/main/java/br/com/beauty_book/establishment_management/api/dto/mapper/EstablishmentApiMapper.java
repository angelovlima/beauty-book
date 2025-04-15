package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.*;
import br.com.beauty_book.establishment_management.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class EstablishmentApiMapper {

    public static Establishment toDomain(CreateEstablishmentApiRequest request) {
        return new Establishment(
                null,
                request.name(),
                request.address(),
                request.photoUrl(),
                toOperatingHourDomain(request.operatingHours()),
                toServiceOfferedDomain(request.services()),
                toProfessionalDomain(request.professionals())
        );
    }

    public static EstablishmentApiResponse toResponse(Establishment establishment) {
        return new EstablishmentApiResponse(
                establishment.getId(),
                establishment.getName(),
                establishment.getAddress(),
                establishment.getPhotoUrl()
        );
    }

    private static List<OperatingHour> toOperatingHourDomain(List<OperatingHourRequest> list) {
        return list.stream()
                .map(req -> new OperatingHour(null,
                        req.dayOfWeek(),
                        LocalTime.parse(req.startTime()),
                        LocalTime.parse(req.endTime())))
                .collect(Collectors.toList());
    }

    private static List<ServiceOffered> toServiceOfferedDomain(List<ServiceOfferedRequest> list) {
        return list.stream()
                .map(req -> new ServiceOffered(null,
                        req.name(),
                        req.description(),
                        BigDecimal.valueOf(req.price()),
                        req.durationMinutes()))
                .collect(Collectors.toList());
    }

    private static List<ProfessionalEstablishment> toProfessionalDomain(List<ProfessionalIdRequest> list) {
        return list.stream()
                .map(req -> new ProfessionalEstablishment(null, req.professionalId()))
                .collect(Collectors.toList());
    }

    public static Establishment toDomain(UpdateEstablishmentApiRequest request) {
        return new Establishment(
                null,
                request.name(),
                request.address(),
                request.photoUrl(),
                toOperatingHourDomain(request.operatingHours()),
                toServiceOfferedDomain(request.services()),
                toProfessionalDomain(request.professionals())
        );
    }

}
