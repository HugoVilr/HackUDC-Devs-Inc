package projectbackend.modelo.entidades;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrabajadorDao extends CrudRepository<Trabajador, Long>, ListPagingAndSortingRepository<Trabajador, Long> {
    List<Trabajador> findByEspecialidadesNombreIn(List<String> nombre);
    @Query("SELECT t FROM Trabajador t LEFT JOIN FETCH t.especialidades WHERE t.id = :id")
    Optional<Trabajador> findByIdWithEspecialidades(@Param("id") Long id);
}
