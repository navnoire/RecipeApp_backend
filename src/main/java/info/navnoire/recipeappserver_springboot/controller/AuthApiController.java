package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.controller.payload.response.ErrorMessageResponse;
import info.navnoire.recipeappserver_springboot.domain.user.Role;
import info.navnoire.recipeappserver_springboot.domain.user.Role.ERole;
import info.navnoire.recipeappserver_springboot.domain.user.User;
import info.navnoire.recipeappserver_springboot.controller.payload.request.LoginRequest;
import info.navnoire.recipeappserver_springboot.controller.payload.request.SignupRequest;
import info.navnoire.recipeappserver_springboot.controller.payload.response.JwtResponse;
import info.navnoire.recipeappserver_springboot.controller.payload.response.MessageResponse;
import info.navnoire.recipeappserver_springboot.repository.user.RoleRepository;
import info.navnoire.recipeappserver_springboot.repository.user.UserRepository;
import info.navnoire.recipeappserver_springboot.security.UserDetailsImpl;
import info.navnoire.recipeappserver_springboot.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Victoria Berezina on 20/06/2021 in RecipeApp project
 */

@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new JwtResponse(
                jwtToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles),
                HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Error: Username is already taken!"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("Error: Email is already in use!"), HttpStatus.BAD_REQUEST);
        }

        //new user creation
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.USER).orElseThrow(() -> new RuntimeException("Error: Role USER is not found"));
        roleSet.add(userRole);
        user.setRoles(roleSet);
        userRepository.save(user);

        return new ResponseEntity<>(new ErrorMessageResponse(HttpStatus.OK.value(), new Date(), "User registered successfully!"), HttpStatus.OK);
    }
}
