package org.mkh.frm.web.rest.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.common.core.dozerMapper.ModelMapper;
import org.mkh.frm.common.web.RequestResponseView;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.service.security.IUserService;
import org.mkh.frm.web.viewModel.security.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/security/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestResponseView(UserViewModel.class)
    @GetMapping(value = "/list")
    public PagingResult<User> listGrid(PagingRequest searchOption) {
        return userService.getAllGrid(searchOption);
    }


    @GetMapping(value = "/authenitacedUser")
    public UserViewModel getAuthenticatedUser() {
        User user = userService.loadByEntityId(SecurityUtility.getAuthenticatedUserId());

        UserViewModel userViewModel = ModelMapper.map(user, UserViewModel.class);
        userViewModel.setAuthorities(user.getGroups().stream().map(Group::getCode).collect(Collectors.toSet()));
        return userViewModel;
    }

    @GetMapping(value = "/{id}")
    @RequestResponseView(UserViewModel.class)
    public User load(@PathVariable int id) {
        return userService.loadByEntityId(id);
    }

    @DeleteMapping(value = "/{id}")
    public Boolean delete(@PathVariable int id) {
        return userService.deleteByEntityId(id);
    }


    @PostMapping(value = "/changePassword")
    public Boolean changePassword(@RequestBody String password) {
        return userService.changePassword(password);
    }

    @GetMapping(value = "/public/checkUsername/{username}")
    public ResponseEntity<String> checkUsernameExistance(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null)
            return ResponseEntity.badRequest().body(null);
        else
            return new ResponseEntity<String>(HttpStatus.OK);
    }


}