package sit.syone.itbkkapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.syone.itbkkapi.dtos.users.AccessTokenDTO;
import sit.syone.itbkkapi.dtos.users.LoginUserDTO;
import sit.syone.itbkkapi.dtos.users.UserDetailsDTO;
import sit.syone.itbkkapi.exceptions.LoginInvalidException;
import sit.syone.itbkkapi.userdatasource.repositories.UserRepository;
import sit.syone.itbkkapi.util.JwtUtils;

@Service
public class AuthenticationService {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;

    public AccessTokenDTO loginUser(LoginUserDTO loginUserDTO) throws LoginInvalidException {
        UserDetailsDTO user = null;
        try {
            user = userService.loadUserByUsername(loginUserDTO.getUsername());
        }catch (UsernameNotFoundException ex){
            throw new LoginInvalidException("Username or Password is incorrect.");
        }
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
        if (!argon2PasswordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
            throw new LoginInvalidException("Username or Password is incorrect.");
        }
        return new AccessTokenDTO(jwtUtils.generateToken(user));
    }
}
