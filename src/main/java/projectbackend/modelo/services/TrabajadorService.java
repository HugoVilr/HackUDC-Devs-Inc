package projectbackend.modelo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import projectbackend.modelo.entidades.Trabajador;
import projectbackend.modelo.entidades.TrabajadorDao;
import projectbackend.modelo.entidades.EspecialidadesDao;
import projectbackend.modelo.excepciones.BadRequestException;
import projectbackend.modelo.excepciones.InternalServerError;
import projectbackend.modelo.excepciones.ResourceNotFoundEception;

import java.util.List;
import java.util.Set;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorDao trabajadorDao;

    @Autowired
    private EspecialidadesDao especialidadesDao;

    @Autowired
    private IAService iaService; // Servicio para llamar a la IA

    public List<Trabajador> buscarTrabajadores(String inputUsuario) throws BadRequestException, ResourceNotFoundEception{
        if (inputUsuario == null || inputUsuario.isBlank()) {
            throw new BadRequestException("El input no puede estar vacío.");
        }
        try {
            Set<String> etiquetas = convertirInputAetiquetas(inputUsuario);
            if (etiquetas.isEmpty()) {
                throw new BadRequestException("No se generaron etiquetas a partir del input.");
            }
            Set<String> especialidadesIA = iaService.obtenerEspecialidades(etiquetas);
            if (especialidadesIA == null || especialidadesIA.isEmpty()) {
                throw new ResourceNotFoundEception("La IA no devolvió especialidades relacionadas.");
            }
            List<Trabajador> trabajadores = trabajadorDao.findByEspecialidadesNombre(especialidadesIA);
            if (trabajadores.isEmpty()) {
                throw new ResourceNotFoundEception("No se encontraron trabajadores con esas especialidades.");
            }
            return trabajadores;
        } catch (BadRequestException | ResourceNotFoundEception e) {
            throw e; // Se propaga al Controller
        } catch (Exception e) {
            throw new InternalServerError("Error interno en la búsqueda de trabajadores.");
        }
    }

    private Set<String> convertirInputAetiquetas(String input) {
        // Simplificación: separar por espacios y convertir a minúsculas
        return Set.of(input.toLowerCase().split(" "));
    }
}
