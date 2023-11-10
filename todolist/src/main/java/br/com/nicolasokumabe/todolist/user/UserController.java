package br.com.nicolasokumabe.todolist.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.nicolasokumabe.todolist.ErrorResponse;
import br.com.nicolasokumabe.todolist.PasswordChangeModel;

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

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserModel userModel) {
        
        if (userModel.getName() == null || userModel.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1001, "Nome é um campo obrigatório"));
        }

        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1002, "Username é um campo obrigatório"));
        }

        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1003, "Senha é um campo obrigatório"));
        }

        var user = this.userRepository.findByUsername(userModel.getUsername());
        
        if (user != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(1004, "Usuário já existe"));
        }        
        
        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }

    @PatchMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody PasswordChangeModel passwordChangeModel) {
        UserModel user = this.userRepository.findByUsername(passwordChangeModel.getUsername());
        if (user == null || !BCrypt.verifyer().verify(passwordChangeModel.getCurrentPassword().toCharArray(), user.getPassword()).verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(1005, "Credenciais inválidas"));
        }

        var passwordHashred = BCrypt.withDefaults().hashToString(12, passwordChangeModel.getNewPassword().toCharArray());
        user.setPassword(passwordHashred);
        this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse(1007, "Senha alterada com sucesso"));
     }


    @DeleteMapping("/")
    public ResponseEntity deleteUser(@RequestBody UserModel userModel) {
        
        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1002, "Username é um campo obrigatório"));
        }

        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(1003, "Senha é um campo obrigatório"));
        }

        UserModel user = this.userRepository.findByUsername(userModel.getUsername());

        if (user == null || !BCrypt.verifyer().verify(userModel.getPassword().toCharArray(), user.getPassword()).verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(1005, "Credenciais inválidas"));
        }

        this.userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse(1006, "Usuário deletado com sucesso"));
    }
}
