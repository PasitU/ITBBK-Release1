package sit.syone.itbkkapi.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.syone.itbkkapi.dtos.users.AccessTokenDTO;
import sit.syone.itbkkapi.dtos.users.LoginUserDTO;
import sit.syone.itbkkapi.exceptions.LoginInvalidException;
import sit.syone.itbkkapi.services.AuthenticationService;
import sit.syone.itbkkapi.services.UserService;

@RestController
@RequestMapping("/v3/login")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("")
    public ResponseEntity<AccessTokenDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) throws LoginInvalidException {
        return ResponseEntity.ok(authenticationService.loginUser(loginUserDTO));
    }

}
