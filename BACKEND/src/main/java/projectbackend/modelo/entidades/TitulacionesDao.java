package projectbackend.modelo.entidades;

import projectbackend.modelo.entidades.Titulaciones;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface TitulacionesDao extends CrudRepository<Titulaciones, Long>, ListPagingAndSortingRepository<Titulaciones, Long> {
}
