package com.vhaibrother.vehicle_client_mng.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    @NotBlank(message = "Username mandatory")
    private String username;
    @NotBlank(message = "Password mandatory")
    private String password;
}
