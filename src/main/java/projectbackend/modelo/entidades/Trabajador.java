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

    @Column(nullable = false)
    private String titulacion;

    @Column
    private String email;

    @Column
    private String telefono;

    @Column
    private int departamento;

    public Trabajador() {}

    public Trabajador(String nombre, String oficio, String titulacion, String email, String telefono, int departamento) {
        this.nombre = nombre;
        this.oficio = oficio;
        this.titulacion = titulacion;
        this.email = email;
        this.telefono = telefono;
        this.departamento = departamento;
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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getDepartamento() { return departamento; }
    public void setDepartamento(int departamento) { this.departamento = departamento; }
}