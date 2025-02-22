package projectbackend.modelo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import projectbackend.modelo.excepciones.BadRequestException;
import projectbackend.modelo.excepciones.ResourceNotFoundEception;
import projectbackend.modelo.excepciones.InternalServerError;

import java.util.Set;

@Service
public class IAService {

    private final WebClient webClient;

    public IAService() {
        this.webClient = WebClient.create("URL_DE_LA_IA"); // Reemplaza con la URL real
    }

    public Set obtenerEspecialidades(Set<String> etiquetas) {
        try {
            return webClient.post()
                    .uri("/obtener-especialidades-relacionadas-con")
                    .bodyValue(etiquetas)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        if (response.statusCode().value() == 400) {
                            return response.bodyToMono(String.class)
                                    .map(msg -> new BadRequestException("Solicitud incorrecta: " + msg));
                        } else if (response.statusCode().value() == 404) {
                            return response.bodyToMono(String.class)
                                    .map(msg -> new ResourceNotFoundEception("No se encontraron especialidades: " + msg));
                        } else {
                            return response.bodyToMono(String.class)
                                    .map(msg -> new BadRequestException("Error del cliente: " + msg));
                        }
                    })
                    .onStatus(status -> status.is5xxServerError(), response ->
                            response.bodyToMono(String.class)
                                    .map(msg -> new InternalServerError("Error interno del servidor de la IA: " + msg))
                    )
                    .bodyToMono(Set.class)
                    .block();

        } catch (WebClientResponseException e) {
            // Manejo de errores HTTP específicos
            if (e.getStatusCode().value() == 400) {
                throw new BadRequestException("Error 400 - Solicitud incorrecta: " + e.getResponseBodyAsString());
            } else if (e.getStatusCode().value() == 404) {
                throw new ResourceNotFoundEception("Error 404 - No encontrado: " + e.getResponseBodyAsString());
            } else if (e.getStatusCode().value() == 500) {
                throw new InternalServerError("Error 500 - Error interno del servidor: " + e.getResponseBodyAsString());
            } else {
                throw new InternalServerError("Error desconocido: " + e.getResponseBodyAsString());
            }

        } catch (Exception e) {
            // Otros errores generales
            throw new InternalServerError("Error inesperado al comunicarse con la IA.");
        }
    }
}
