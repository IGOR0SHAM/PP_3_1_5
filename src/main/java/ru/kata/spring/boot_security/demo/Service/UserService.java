package ru.kata.spring.boot_security.demo.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    List<User> getAllUsers();

    User getById(Long id);

    void save(User user);

    void update(User user);

    void delete(Long id);

    User findByUsername(String username);
}