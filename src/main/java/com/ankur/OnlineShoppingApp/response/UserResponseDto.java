package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {
    private int id;
    private String name;
    private String email;
    private String username;
    private Role role;
}
