package projectbackend.modelo.entidades;

import projectbackend.modelo.entidades.Trabajador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface TrabajadorDao extends CrudRepository<Trabajador, Long>, ListPagingAndSortingRepository<Trabajador, Long> {
}
