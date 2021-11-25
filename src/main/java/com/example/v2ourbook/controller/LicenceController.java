package com.example.v2ourbook.controller;

import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.dto.LicenceDto;
import com.example.v2ourbook.repositories.LicenceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LicenceController {

    LicenceRepository licenceRepository;

    public LicenceController(LicenceRepository licenceRepository) {
        this.licenceRepository = licenceRepository;
    }

    @GetMapping("/licence/{id}")
    public LicenceDto getById(@PathVariable(name = "id") Long id) throws ExceptionBlueprint {
        return new LicenceDto(licenceRepository.findById(id)
                .orElseThrow(() -> new ExceptionBlueprint("book not found","no",1)));
    }

}
