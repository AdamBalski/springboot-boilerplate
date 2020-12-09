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
    private AdminService adminService;

    @Autowired
    private void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "/api/admin/get-user-by-uuid")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public User getUserDataByUUID(@RequestParam UUID uuid) {
        return adminService.getUserDataByUUID(uuid);
    }

    @GetMapping(value = "/api/admin/get-user-by-login")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public User getUserDataByLogin(@RequestParam String login) {
        return adminService.getUserDataByLogin(login);
    }

    @DeleteMapping(value = "/api/admin/delete-user")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public void deleteUser(@RequestBody String login) {
        adminService.deleteUserByLogin(login);
    }
}
