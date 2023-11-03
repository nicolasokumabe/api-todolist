package br.com.nicolasokumabe.todolist.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Modificador
 * public
 * private
 * protected
 */
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    /**
     * String (texto)
     * Integer (int) numeros inteiros
     * Double (double) números 0.0000
     * Float (float) números 0.000
     * A C  (char)
     * Date (data)
     * void
     */
    /*
     * Body
     */

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserModel userModel) {
        
        if (userModel.getName() == null || userModel.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Nome é um campo obrigatório");
        }

        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Username é um campo obrigatório");
        }

        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Senha é um campo obrigatório");
        }

        var user = this.userRepository.findByUsername(userModel.getUsername());
        
        if (user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Usuário já existe");
        }        
        
        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body("Sucesso: Usuário criado com sucesso");
    }

    @DeleteMapping("/")
    public ResponseEntity deleteUser(@RequestBody UserModel userModel) {
        
        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Username é um campo obrigatório");
        }

        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Senha é um campo obrigatório");
        }

        UserModel user = this.userRepository.findByUsername(userModel.getUsername());

        if (user == null || !BCrypt.verifyer().verify(userModel.getPassword().toCharArray(), user.getPassword()).verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: Credenciais inválidas");
        }

        this.userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body("Sucesso: Usuário deletado com sucesso");
    }
}
