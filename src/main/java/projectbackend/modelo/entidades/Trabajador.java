package projectbackend.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String oficio;

    @Column
    private String titulacion;

    public Trabajador() {}

    public Trabajador(String nombre, String oficio, String titulacion) {
        this.nombre = nombre;
        this.oficio = oficio;
        this.titulacion = titulacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getOficio() { return oficio; }
    public void setOficio(String oficio) { this.oficio = oficio; }

    public String getTitulacion() { return titulacion; }
    public void setTitulacion(String titulacion) { this.titulacion = titulacion; }
}