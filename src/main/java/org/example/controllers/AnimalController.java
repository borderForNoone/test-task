package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.xml.bind.JAXBException;
import org.example.domain.AnimalDto;
import org.example.domain.AnimalEntity;
import org.example.mappers.Mapper;
import org.example.services.AnimalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Test task", description = "Rest APIs")
@RestController
@RequestMapping("/files/uploads")
public class AnimalController {
    private final AnimalService service;
    private final Mapper<AnimalEntity, AnimalDto> animalMapper;

    public AnimalController(AnimalService service, Mapper<AnimalEntity, AnimalDto> animalMapper) {
        this.service = service;
        this.animalMapper = animalMapper;
    }

    @Operation(summary = "Get filtered and sorted animals," +
            " example format:(http://localhost:8080/files/uploads?page=0&size=7&sort=id,desc&type=cat)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered and sorted animals"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AnimalDto>> getFilteredAndSortedAnimals(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "Type of animal")
            @RequestParam(required = false) String type,
            @Parameter(description = "Category of animal")
            @RequestParam(required = false) String category,
            @Parameter(description = "Sex of animal")
            @RequestParam(required = false) String sex
    ) {
        try {
            Page<AnimalEntity> animalEntities = service.getFilteredAndSortedAnimals(pageable, type, category, sex);

            Page<AnimalDto> animalDtos = animalEntities.map(animalEntity -> {
                AnimalDto animalDto = animalMapper.mapTo(animalEntity);
                return animalDto;
            });

            return ResponseEntity.ok(animalDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Upload animals from file (CSV or XML)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully uploaded animals"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Set<AnimalDto>> uploadAnimals(
            @Parameter(description = "The file containing animals to upload", schema = @Schema(type = "string", format = "binary"))
            @RequestPart("file") MultipartFile file
    ) throws IOException, JAXBException {
        Set<AnimalDto> savedAnimals = service.uploadAnimals(file).stream()
                .map(animalDto -> animalMapper.mapFrom(animalDto))
                .peek(animal -> service.createAnimal(animal))
                .map(animalEntity -> {
                    AnimalDto animalDto = animalMapper.mapTo(animalEntity);
                    animalDto.setId(animalEntity.getId());
                    return animalDto;
                })
                .collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimals);
    }
}
