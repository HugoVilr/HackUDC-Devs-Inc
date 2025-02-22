package projectbackend.modelo.entidades;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String email;

    @Column
    private String telefono;

    // Relación muchos a muchos con Especialidad
    @ManyToMany
    @JoinTable(
            name = "usuario_especialidad", // Tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"), // Clave foránea en la tabla intermedia
            inverseJoinColumns = @JoinColumn(name = "especialidad_id") // Clave foránea en la tabla intermedia
    )
    private Set<Especialidades> especialidades;

    // Relación muchos a muchos con Titulacion
    @ManyToMany
    @JoinTable(
            name = "usuario_titulacion", // Tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"), // Clave foránea en la tabla intermedia
            inverseJoinColumns = @JoinColumn(name = "titulacion_id") // Clave foránea en la tabla intermedia
    )
    private Set<Titulaciones> titulaciones;

    public Trabajador() {}

    public Trabajador(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Set<Especialidades> getEspecialidades() {
        return especialidades;
    }
}