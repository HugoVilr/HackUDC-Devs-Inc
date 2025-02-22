package projectbackend.modelo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbackend.modelo.entidades.Trabajador;
import projectbackend.modelo.excepciones.BadRequestException;
import projectbackend.modelo.excepciones.InternalServerError;
import projectbackend.modelo.excepciones.ResourceNotFoundEception;
import projectbackend.modelo.services.TrabajadorService;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class Controller {

    @Autowired
    private TrabajadorService trabajadorService;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarTrabajadores(@RequestParam String input) {
        try {
            List<Trabajador> trabajadores = trabajadorService.buscarTrabajadores(input);
            return ResponseEntity.ok(trabajadores);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundEception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

