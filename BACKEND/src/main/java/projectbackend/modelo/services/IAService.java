package projectbackend.modelo.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import projectbackend.modelo.excepciones.BadRequestException;
import projectbackend.modelo.excepciones.ResourceNotFoundEception;
import projectbackend.modelo.excepciones.InternalServerError;

import java.util.Set;

@Service
public class IAService {

    private final WebClient webClient;
    private final HttpServletResponse httpServletResponse;

    public IAService(HttpServletResponse httpServletResponse) {
        // Configura la URL base correctamente (incluye la clave de API)
        this.webClient = WebClient.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyCtwzcNT0EVkbYYsvZfkIa_cpMNOzFx_2I");
        this.httpServletResponse = httpServletResponse;
    }

    public Set obtenerEspecialidades(Object etiquetas) {
        System.out.println(etiquetas.toString());
        try {
            // Construir el cuerpo de la solicitud JSON
            String requestBody = "{\n" +
                    "    \"contents\": [\n" +
                    "        {\n" +
                    "            \"parts\": [\n" +
                    "                {\"text\": \"Explain how AI works\"}\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            // Realizar la solicitud POST con el cuerpo JSON y la clave de API
            return webClient.post()
                    .uri(UriBuilder::build)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        System.out.println(response);
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
            throw new InternalServerError("Error inesperado al comunicarse con la IA."+ e.getMessage());
        }
    }
}