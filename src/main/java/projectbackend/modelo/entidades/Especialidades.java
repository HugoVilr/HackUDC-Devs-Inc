package projectbackend.modelo.entidades;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "especialidades")
public class Especialidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    public Especialidades() {}

    public Especialidades(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @ManyToMany(mappedBy = "especialidades")
    private Set<Trabajador> usuarios;
}
