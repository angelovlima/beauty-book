package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.gateway.OperatingHourGateway;
import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetOperatingHoursByEstablishmentIdUseCase {

    private final OperatingHourGateway gateway;

    public GetOperatingHoursByEstablishmentIdUseCase(OperatingHourGateway gateway) {
        this.gateway = gateway;
    }

    public List<OperatingHour> execute(Long establishmentId) {
        return gateway.findByEstablishmentId(establishmentId);
    }
}
