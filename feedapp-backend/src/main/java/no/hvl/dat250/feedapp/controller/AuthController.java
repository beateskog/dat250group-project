package no.hvl.dat250.feedapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import no.hvl.dat250.feedapp.dto.authentication.AccountRespDTO;
import no.hvl.dat250.feedapp.dto.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.dto.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.service.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO request){
        try {
            var response = authService.register(request);
		    return ResponseEntity.ok(response);
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
	
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            return ResponseEntity.ok(authService.authenticate(authRequest));
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> getUser(UsernamePasswordAuthenticationToken token) {
        try {
            Account user = (Account) token.getPrincipal();
            AccountRespDTO accountRespDTO = new AccountRespDTO();
            accountRespDTO.setUsername(user.getUsername());
            return ResponseEntity.ok(accountRespDTO);

        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

}
