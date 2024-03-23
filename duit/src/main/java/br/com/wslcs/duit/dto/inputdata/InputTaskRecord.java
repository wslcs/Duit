package br.com.wslcs.duit.dto.inputdata;

import jakarta.validation.constraints.NotBlank;

public record InputTaskRecord(@NotBlank String title,@NotBlank String description) {
    
}
