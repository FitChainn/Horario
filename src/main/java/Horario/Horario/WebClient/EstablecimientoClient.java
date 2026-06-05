package Horario.Horario.WebClient;

import Horario.Horario.Dto.EstablecimientoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EstablecimientoClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${establecimiento.service.url}")
    private String establecimientoServiceUrl;

    public EstablecimientoDTO obtenerEstablecimientoPorId(Long establecimientoId) {
        log.info("Obteniendo establecimiento con id {}", establecimientoId);
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(establecimientoServiceUrl + "/v1/establecimientos/" + establecimientoId)
                    .retrieve()
                    .bodyToMono(EstablecimientoDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            log.error("Establecimiento con id {} no encontrado", establecimientoId);
            throw new NoSuchElementException("Establecimiento con id " + establecimientoId + " no encontrado");
        } catch (Exception e) {
            log.warn("No se pudo obtener establecimiento con ID: {}", establecimientoId);
            return null;
        }
    }
}