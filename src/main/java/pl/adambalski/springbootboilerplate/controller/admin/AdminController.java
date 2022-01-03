package pl.adambalski.springbootboilerplate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.adambalski.springbootboilerplate.model.User;
import pl.adambalski.springbootboilerplate.service.AdminService;

import java.util.UUID;

/**
 * AdminController <br><br>
 *
 * @see RestController
 * @author Adam Balski
 */
@RestController
public class AdminController {
    private final AdminService adminService;

    @Autowired
    AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "/api/admin/get-user-by-uuid")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public User getUserByUUID(@RequestParam UUID uuid) {
        User user = adminService.getUserByUUID(uuid);
        removePasswordInformation(user);
        return user;
    }

    @GetMapping(value = "/api/admin/get-user-by-login")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public User getUserByLogin(@RequestParam String login) {
        User user = adminService.getUserByLogin(login);
        removePasswordInformation(user);
        return user;
    }

    @DeleteMapping(value = "/api/admin/delete-user")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public void deleteUserByLogin(@RequestBody String login) {
        adminService.deleteByLogin(login);
    }

    private void removePasswordInformation(User user) {
        user.setPassword(null);
    }
}
