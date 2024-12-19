package com.pedromartins.backend_challenge.services;
import com.pedromartins.backend_challenge.dto.LoginUserDto;
import com.pedromartins.backend_challenge.dto.RecoveryJwtTokenDto;
import com.pedromartins.backend_challenge.dto.UserDto;
import com.pedromartins.backend_challenge.exceptions.UnauthorizedException;
import com.pedromartins.backend_challenge.models.*;
import com.pedromartins.backend_challenge.repositories.UserRepository;
import com.pedromartins.backend_challenge.security.UserAuthenticationFilter;
import com.pedromartins.backend_challenge.user.UserDetailsImpl;
import com.pedromartins.backend_challenge.security.UserAuthenticationFilter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pedromartins.backend_challenge.requests.UserRequest;
import com.pedromartins.backend_challenge.repositories.RoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserRequest request) {
        User customer = new User();
        try {
            customer.setName(request.getName());
        } catch (Exception e) {
            throw new RuntimeException("Erro on creating user: " + e.getMessage(), e);
        }
        customer.setCpf(request.getCpf());

        List<Email> emails = request.getEmails().stream()
                .map(emailRequest -> {
                    Email email = new Email();
                    email.setEmail(emailRequest.getEmail());
                    email.setUser(customer);
                    return email;
                }).collect(Collectors.toList());
        customer.setEmails(emails);

        List<Phone> phones = request.getPhones().stream()
                .map(phoneRequest -> {
                    Phone phone = new Phone();
                    phone.setPhone(phoneRequest.getNumber());
                    phone.setType(phoneRequest.getType());
                    phone.setUser(customer);
                    return phone;
                }).collect(Collectors.toList());
        customer.setPhones(phones);
        if (request.getAddress() != null) {
            Address address = new Address();
            address.setCep(request.getAddress().getCep());
            address.setPatio(request.getAddress().getPatio());
            address.setNeighborhood(request.getAddress().getNeighborhood());
            address.setCity(request.getAddress().getCity());
            address.setUf(request.getAddress().getUf());
            address.setComplement(request.getAddress().getComplement());
            address.setUser(customer);
            customer.setAddress(address);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        customer.setPassword(encodedPassword);

        List<Role> roles = request.getRole().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toList());
        customer.setRoles(roles);
        return userRepository.save(customer);
    }

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public boolean isAuthenticated () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public boolean userIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMINISTRATOR"));
        }
        return false;
    }

    public List<UserDto> findAllUsers() {
        if (!isAuthenticated()) {
            throw new UnauthorizedException();
        }
        if (!userIsAdmin()) {
            throw new UnauthorizedException();
        }
        try {
            List<User> users = userRepository.findAll();
            return users.stream().map(user -> {
                if (user.getCpf() != null) {
                    String cpf = user.getCpf();
                    String cpfMasked = "XXX.XXX.XXX-" + cpf.substring(cpf.length() - 2);
                    user.setCpf(cpfMasked);
                }
                if (user.getPhones() != null) {
                    List<Phone> phones = user.getPhones();
                    List<String> maskedPhones = phones.stream()
                            .map(phone -> {
                                String phoneNumber = phone.getPhone();
                                if (phoneNumber != null && phoneNumber.length() > 2) {
                                    return phoneNumber.substring(0, phoneNumber.length() - 2).replaceAll(".", "X")
                                            + phoneNumber.substring(phoneNumber.length() - 2);
                                } else {
                                    return phoneNumber;
                                }
                            })
                            .collect(Collectors.toList());
                    UserDto userDto = new UserDto(user);
                    userDto.setPhone(maskedPhones);
                    return userDto;
                }
                return new UserDto(user);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error on searching users: " + e.getMessage(), e);
        }
    }


    public UserDto findMyUser(HttpServletRequest request) {
        String token = userAuthenticationFilter.getRecoveryToken(request);
        if (token == null) {
            throw new UnauthorizedException("Token not found");
        }
        try {
            String subject = jwtTokenService.getSubjectFromToken(token).trim();
            User user = userRepository.findByEmails_Email(subject)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + subject));

            if (user.getCpf() != null) {
                String cpf = user.getCpf();
                String cpfMasked = "XXX.XXX.XXX-" + cpf.substring(cpf.length() - 2);
                user.setCpf(cpfMasked);
            }

            if (user.getPhones() != null) {
                List<Phone> phones = user.getPhones();
                List<String> maskedPhones = phones.stream()
                        .map(phone -> {
                            String phoneNumber = phone.getPhone();
                            if (phoneNumber != null && phoneNumber.length() > 2) {
                                return phoneNumber.substring(0, phoneNumber.length() - 2).replaceAll(".", "X")
                                        + phoneNumber.substring(phoneNumber.length() - 2);
                            } else {
                                return phoneNumber;
                            }
                        })
                        .collect(Collectors.toList());

                UserDto userDto = new UserDto(user);
                userDto.setPhone(maskedPhones);
                return userDto;
            }

            return new UserDto(user);
        } catch (Exception e) {
            throw new UnauthorizedException("Error during user retrieval: " + e.getMessage());
        }
    }
}
