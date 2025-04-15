package br.com.beauty_book.professional_management.api.dto.mapper;

import br.com.beauty_book.professional_management.api.dto.*;
import br.com.beauty_book.professional_management.domain.model.*;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;

import java.time.LocalTime;
import java.util.List;

public class ProfessionalApiMapper {

    private ProfessionalApiMapper() {}

    public static Professional toDomain(CreateProfessionalApiRequest request) {
        return new Professional(
                null,
                request.name(),
                new Cpf(request.cpf()),
                request.phoneNumber(),
                request.email() != null ? new Email(request.email()) : null,
                toAvailabilityList(request.availabilityList()),
                toServiceOfferedList(request.services())
        );
    }

    public static Professional toDomain(UpdateProfessionalApiRequest request, Long id) {
        return new Professional(
                id,
                request.name(),
                new Cpf(request.cpf()),
                request.phoneNumber(),
                request.email() != null ? new Email(request.email()) : null,
                toAvailabilityList(request.availabilityList()),
                toServiceOfferedList(request.services())
        );
    }

    public static ProfessionalApiResponse toResponse(Professional professional) {
        return new ProfessionalApiResponse(
                professional.id(),
                professional.name(),
                professional.cpf().getValue(),
                professional.phoneNumber(),
                professional.email() != null ? professional.email().getValue() : null
        );
    }

    private static List<Availability> toAvailabilityList(List<AvailabilityRequest> requests) {
        return requests.stream()
                .map(r -> new Availability(null,
                        r.dayOfWeek(),
                        LocalTime.parse(r.startTime()),
                        LocalTime.parse(r.endTime())))
                .toList();
    }

    private static List<ProfessionalServiceOffered> toServiceOfferedList(List<ServiceOfferedIdRequest> requests) {
        return requests.stream()
                .map(r -> new ProfessionalServiceOffered(null, r.id()))
                .toList();
    }
}
