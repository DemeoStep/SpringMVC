package com.oched.booksprj.controllers;

import com.oched.booksprj.requests.ActionRequest;
import com.oched.booksprj.requests.EditUserRequest;
import com.oched.booksprj.requests.NewUserRequest;
import com.oched.booksprj.responses.UserInfoResponse;
import com.oched.booksprj.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-rest")
public class RestUserController {
    private final UserService userService;

    @GetMapping("/list")
    public UserInfoResponse[] getAllUsers (){
        return this.userService.getUsersList(true).toArray(new UserInfoResponse[0]);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addNewUser(final @Valid @RequestBody NewUserRequest request) {
        this.userService.addNewUser(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> editUser(final @Valid @RequestBody EditUserRequest request) {
        this.userService.editUser(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(final @PathVariable Long id) {
        this.userService.deleteUser(new ActionRequest(id));
        return ResponseEntity.ok().build();
    }
}
