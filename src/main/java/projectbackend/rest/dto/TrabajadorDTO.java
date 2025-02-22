package projectbackend.rest.dto;
import java.util.List;
public class TrabajadorDTO {
    private Long id;
    private String nombre;
    private String email;
    // Suponiendo que queremos enviar los nombres de las especialidades
    private List<String> especialidades;

    public TrabajadorDTO() {
    }

    public TrabajadorDTO(Long id, String nombre, String email, List<String> especialidades) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.especialidades = especialidades;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

}
