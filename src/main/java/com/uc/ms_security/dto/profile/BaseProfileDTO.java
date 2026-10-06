package com.uc.ms_security.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseProfileDTO {

    @NotBlank(
            message = "El teléfono es obligatorio"
    )
    @Size(
            min = 7,
            max = 30,
            message = "El teléfono debe tener entre 7 y 30 caracteres"
    )
    private String phone;

    @NotNull(
            message = "La fecha de nacimiento es obligatoria"
    )
    private Date birthDate;
}