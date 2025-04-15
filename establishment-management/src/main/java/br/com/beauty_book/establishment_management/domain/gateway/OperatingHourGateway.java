package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.domain.model.OperatingHour;

import java.util.List;

public interface OperatingHourGateway {
    List<OperatingHour> findByEstablishmentId(Long establishmentId);
}
