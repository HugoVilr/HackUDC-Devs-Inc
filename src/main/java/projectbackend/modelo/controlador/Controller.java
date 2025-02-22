package projectbackend.modelo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbackend.modelo.entidades.Trabajador;
import projectbackend.modelo.excepciones.BadRequestException;
import projectbackend.modelo.excepciones.InternalServerError;
import projectbackend.modelo.excepciones.ResourceNotFoundEception;
import projectbackend.modelo.services.TrabajadorService;
import projectbackend.rest.dto.TrabajadorDTO;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class Controller {

    @Autowired
    private TrabajadorService trabajadorService;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarTrabajadores(@RequestParam String input) {
        try {
            List<TrabajadorDTO> trabajadores = trabajadorService.buscarTrabajadores(input);
            return ResponseEntity.ok(trabajadores);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundEception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrabajadorDTO> obtenerTrabajador(@PathVariable Long id) {
        TrabajadorDTO trabajador = trabajadorService.obtenerTrabajadorPorId(id);
        return ResponseEntity.ok(trabajador);
    }
}

