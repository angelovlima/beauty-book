package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.ServiceOfferedGateway;
import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import org.springframework.stereotype.Service;

@Service
public class GetServiceOfferedByIdUseCase {

    private final ServiceOfferedGateway gateway;

    public GetServiceOfferedByIdUseCase(ServiceOfferedGateway gateway) {
        this.gateway = gateway;
    }

    public ServiceOffered execute(Long id) {
        return gateway.findById(id)
                .orElseThrow(() -> new ServiceOfferedNotFoundException(id));
    }
}
