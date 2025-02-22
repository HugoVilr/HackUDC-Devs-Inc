package projectbackend.modelo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectbackend.modelo.entidades.Especialidades;
import projectbackend.modelo.entidades.Trabajador;
import projectbackend.modelo.entidades.TrabajadorDao;
import projectbackend.modelo.entidades.EspecialidadesDao;
import projectbackend.modelo.excepciones.BadRequestException;
import projectbackend.modelo.excepciones.InternalServerError;
import projectbackend.modelo.excepciones.ResourceNotFoundEception;
import projectbackend.rest.dto.TrabajadorDTO;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorDao trabajadorDao;

    @Autowired
    private EspecialidadesDao especialidadesDao;

    public List<TrabajadorDTO> buscarTrabajadores(String inputUsuario) throws BadRequestException, ResourceNotFoundEception{
        if (inputUsuario == null || inputUsuario.isBlank()) {

            throw new BadRequestException("El input no puede estar vacío.");
        }

        try {
            List<String> especialidades = Collections.singletonList(inputUsuario);
            List <Trabajador> trabajadores = trabajadorDao.findByEspecialidadesNombreIn(especialidades);
            if (trabajadores.isEmpty()) {
                throw new ResourceNotFoundEception("No se encontraron trabajadores con esas especialidades.");
            }
            return trabajadores.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());
        } catch (BadRequestException | ResourceNotFoundEception e) {
            throw e; // Se propaga al Controller
        } catch (Exception e) {
            throw new InternalServerError(""+ e.getMessage()+ e.getCause());
        }
    }

    public TrabajadorDTO obtenerTrabajadorPorId(Long id) throws ResourceNotFoundEception {
        try {
            Trabajador trabajador = trabajadorDao.findByIdWithEspecialidades(id)
                    .orElseThrow(() -> new ResourceNotFoundEception("Trabajador no encontrado con ID: " + id));

            return convertirADtoCompleto(trabajador);
        } catch (Exception e) {
            throw new InternalServerError("Error al obtener trabajador: " + e.getMessage());
        }
    }

    private TrabajadorDTO convertirADtoCompleto(Trabajador trabajador) {
        List<String> especialidades = trabajador.getEspecialidades()
                .stream()
                .map(Especialidades::getNombre)
                .collect(Collectors.toList());
        return new TrabajadorDTO(trabajador.getId(), trabajador.getNombre(), trabajador.getEmail(), especialidades);
    }

    private TrabajadorDTO convertirADto(Trabajador trabajador) {
        // Mapeo manual: extraemos el nombre de cada especialidad de la colección
        List<String> especialidades = trabajador.getEspecialidades()
                .stream()
                .map(Especialidades::getNombre)
                .collect(Collectors.toList());
        return new TrabajadorDTO(trabajador.getId(), trabajador.getNombre(), trabajador.getEmail(), especialidades);
    }
}
