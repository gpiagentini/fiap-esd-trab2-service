package br.com.gpiagentini.api.adapters.controllers;

import br.com.gpiagentini.api.application.dto.Authentication.AuthenticationData;
import br.com.gpiagentini.api.application.dto.Authentication.TokenData;
import br.com.gpiagentini.api.adapters.external.authentication.TokenService;
import br.com.gpiagentini.api.infraestructure.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Use this to login and generate a token to access the other functionalities.")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Authenticate", description = "Intended to login and retrieve a token.")
    @PostMapping
    public ResponseEntity<TokenData> login(@RequestBody @Valid AuthenticationData authenticationData) {
        var token = new UsernamePasswordAuthenticationToken(authenticationData.login(), authenticationData.password());
        var authentication = authenticationManager.authenticate(token);
        var jwtToken = tokenService.generateToken((UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenData(jwtToken));
    }
}
