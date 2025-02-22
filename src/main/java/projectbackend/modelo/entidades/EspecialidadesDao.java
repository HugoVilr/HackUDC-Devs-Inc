package projectbackend.modelo.entidades;

import projectbackend.modelo.entidades.Especialidades;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
public interface EspecialidadesDao extends CrudRepository<Especialidades, Long>, ListPagingAndSortingRepository<Especialidades, Long> {
}
