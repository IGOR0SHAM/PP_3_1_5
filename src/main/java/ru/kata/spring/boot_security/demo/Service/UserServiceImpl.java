package ru.kata.spring.boot_security.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.Repositories.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Override
    @Transactional
    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setListRole(user.getListRole());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getListRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> listRoles) {
        return listRoles.stream().map(r -> new SimpleGrantedAuthority(r.getRolename())).collect(Collectors.toList());
    }
    @PostConstruct
    private void createRoleAndUser() {

        Role admin = new Role(1L, "ROLE_ADMIN");
        Role user = new Role(2L, "ROLE_USER");
        roleRepository.saveAll(List.of(admin, user));

        List<Role> rolesOfAdmin = new ArrayList<>();
        List<Role> rolesOfUser = new ArrayList<>();

        Collections.addAll(rolesOfAdmin, admin, user);
        Collections.addAll(rolesOfUser, user);

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setListRole(rolesOfAdmin);
        adminUser.setEmail("@admin");
        userRepository.save(adminUser);
        User normalUser = new User();
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode("user"));
        normalUser.setListRole(rolesOfUser);
        normalUser.setEmail("@user");
        userRepository.save(normalUser);
    }
}
