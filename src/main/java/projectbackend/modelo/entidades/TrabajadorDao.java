package projectbackend.modelo.entidades;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface TrabajadorDao extends CrudRepository<Trabajador, Long>, ListPagingAndSortingRepository<Trabajador, Long> {
    List<Trabajador> findByEspecialidadesNombreIn(Set nombre);
}
