package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.controller.exhandling.DuplicateUserDataException;
import info.navnoire.recipeappserver_springboot.controller.exhandling.TokenRefreshException;
import info.navnoire.recipeappserver_springboot.controller.payload.request.TokenRefreshRequest;
import info.navnoire.recipeappserver_springboot.controller.payload.response.MessageResponse;
import info.navnoire.recipeappserver_springboot.controller.payload.response.TokenRefreshResponse;
import info.navnoire.recipeappserver_springboot.domain.user.RefreshToken;
import info.navnoire.recipeappserver_springboot.domain.user.Role;
import info.navnoire.recipeappserver_springboot.domain.user.Role.ERole;
import info.navnoire.recipeappserver_springboot.domain.user.User;
import info.navnoire.recipeappserver_springboot.controller.payload.request.LoginRequest;
import info.navnoire.recipeappserver_springboot.controller.payload.request.SignupRequest;
import info.navnoire.recipeappserver_springboot.controller.payload.response.JwtResponse;
import info.navnoire.recipeappserver_springboot.repository.user.RoleRepository;
import info.navnoire.recipeappserver_springboot.repository.user.UserRepository;
import info.navnoire.recipeappserver_springboot.security.UserDetailsImpl;
import info.navnoire.recipeappserver_springboot.security.jwt.JwtUtils;
import info.navnoire.recipeappserver_springboot.service.RefreshTokenService;
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
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RefreshTokenService refreshTokenService;
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
    public void setRefreshTokenService(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
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
        String jwtAccessToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.findByUserId(userDetails.getId())
                .flatMap(token -> refreshTokenService.verifyTokenExpiration(token))
                .orElseGet(() -> refreshTokenService.createRefreshToken(userDetails.getId()));

        return new ResponseEntity<>(new JwtResponse(
                jwtAccessToken,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles),
                HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new DuplicateUserDataException(signupRequest.getUsername(), "Username is already taken!");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicateUserDataException(signupRequest.getEmail(), "Email is already in use!");
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

        return new ResponseEntity<>(new MessageResponse(HttpStatus.OK.value(), new Date(), "User registered successfully!"), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Token is not found in database!"));

        refreshToken = refreshTokenService.verifyTokenExpiration(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token expired. Please make a new signin request"));

        String accessToken = jwtUtils.generateJwtTokenFromUsername(refreshToken.getUser().getUsername());
        return new ResponseEntity<>(new TokenRefreshResponse(accessToken, requestRefreshToken), HttpStatus.OK);
    }
}
