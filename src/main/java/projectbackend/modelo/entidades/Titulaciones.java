package projectbackend.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "titulacion")
public class Titulaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    public Titulaciones(){
    }

    public Titulaciones(String nombre){
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
