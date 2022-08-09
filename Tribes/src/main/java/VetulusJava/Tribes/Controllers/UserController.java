package VetulusJava.Tribes.Controllers;

import VetulusJava.Tribes.DTOs.*;
import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.DTOs.AuthenticationRequest;
import VetulusJava.Tribes.DTOs.AuthenticationResponseFail;
import VetulusJava.Tribes.DTOs.AuthenticationResponseOk;
import VetulusJava.Tribes.Exceptions.UserAlreadyExistException;
import VetulusJava.Tribes.Security.JwtUtil;
import VetulusJava.Tribes.Security.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import VetulusJava.Tribes.Services.UserService.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserDetailsServiceImpl userDetailsService;
    private IUserService userService;

    public UserController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ApiOperation(value = "User registration", response = UserDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User created")})
    public ResponseEntity<UserDto> register(@Valid @RequestBody CreateUserDto createUser) throws UserAlreadyExistException {
        ApplicationUser registered = userService.registerNewUserAccount(createUser, passwordEncoder.encode(createUser.getPassword()));
        UserDto newUser = new UserDto(registered.getId().toString(), registered.getName().toString(), registered.getKingdom().getId().toString());
        return new ResponseEntity<UserDto>(newUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "User login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        ArrayList<String> missingParameters = new ArrayList();
        String message;
        if (StringUtils.isEmpty(authenticationRequest.getLogin())) {
            missingParameters.add("login");
        }
        if (StringUtils.isEmpty(authenticationRequest.getPassword())) {
            missingParameters.add("password");
        }
        if (!missingParameters.isEmpty()) {
            message = "Missing parameter(s): ";
            for (String parameter : missingParameters) {
                message += parameter + ", ";
            }
            return ResponseEntity.status(400).body(new AuthenticationResponseFail(message));
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.
                    getLogin(), authenticationRequest.getPassword()));
        } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AuthenticationResponseFail("Invalid combination of login and password"));
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponseOk(jwt));
    }
}

