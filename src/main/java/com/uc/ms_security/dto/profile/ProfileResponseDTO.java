package com.uc.ms_security.dto.profile;

import lombok.Value;

import java.util.Date;

@Value
public class ProfileResponseDTO {
    Long id;
    String phone;
    Date birthDate;
}