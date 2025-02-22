package projectbackend.modelo.entidades;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projectbackend.modelo.entidades.Trabajador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface TrabajadorDao extends CrudRepository<Trabajador, Long>, ListPagingAndSortingRepository<Trabajador, Long> {
    List<Trabajador> findByEspecialidadesNombre(Set especialidad);
}
