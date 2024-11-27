package org.finlelab.beko.service;

import org.finlelab.beko.dto.UserDTO;
import org.finlelab.beko.entity.User;


public interface UserService {
    void saveUser(UserDTO userDto);


    User findByUsername(String username);

}