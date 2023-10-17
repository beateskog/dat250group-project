package no.hvl.dat250.feedapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;


import no.hvl.dat250.feedapp.DTO.authentication.AccountRespDTO;
import no.hvl.dat250.feedapp.DTO.authentication.AuthRequestDTO;
import no.hvl.dat250.feedapp.DTO.authentication.AuthResponseDTO;
import no.hvl.dat250.feedapp.DTO.authentication.RegisterRequestDTO;
import no.hvl.dat250.feedapp.exception.BadRequestException;
import no.hvl.dat250.feedapp.model.Account;
import no.hvl.dat250.feedapp.service.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody RegisterRequestDTO request){
        var response = authService.register(request);
		return ResponseEntity.ok(response);
    }
	
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            return ResponseEntity.ok(authService.authenticate(authRequest));
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
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
        }
    }

}
