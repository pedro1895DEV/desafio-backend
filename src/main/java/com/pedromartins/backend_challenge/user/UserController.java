package com.pedromartins.backend_challenge.user;

import com.pedromartins.backend_challenge.dto.LoginUserDto;
import com.pedromartins.backend_challenge.dto.RecoveryJwtTokenDto;
import com.pedromartins.backend_challenge.dto.UserDto;
import com.pedromartins.backend_challenge.models.User;
import com.pedromartins.backend_challenge.repositories.RoleRepository;
import com.pedromartins.backend_challenge.repositories.UserRepository;
import com.pedromartins.backend_challenge.requests.UserRequest;
import com.pedromartins.backend_challenge.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User savedCustomer = userService.createUser(request);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto ) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findUser() {
        List<UserDto> userDtos = userService.findAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/my-user")
    public ResponseEntity<UserDto> findMyUser(HttpServletRequest request) {
        UserDto userDto = userService.findMyUser(request);
        return ResponseEntity.ok(userDto);
    }

}
