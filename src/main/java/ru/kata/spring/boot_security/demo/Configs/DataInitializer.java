package ru.kata.spring.boot_security.demo.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.Service.RoleService;
import ru.kata.spring.boot_security.demo.Service.UserService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DataInitializer {


    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @PostConstruct
    public void createRoleAndUser() {

        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        roleService.saveRole(admin);
        roleService.saveRole(user);
        List<Role> rolesOfAdmin = new ArrayList<>();
        List<Role> rolesOfUser = new ArrayList<>();
        Collections.addAll(rolesOfAdmin, admin, user);
        Collections.addAll(rolesOfUser, user);
        User user1 = new User("admin", "admin", "admin", rolesOfAdmin);
        User user2 = new User("user", "user", "user", rolesOfUser);
        userService.save(user1);
        userService.save(user2);

    }
}
