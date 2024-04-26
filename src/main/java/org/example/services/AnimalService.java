package org.example.services;

import jakarta.xml.bind.JAXBException;
import org.example.domain.AnimalDto;
import org.example.domain.AnimalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface AnimalService {
    AnimalEntity createAnimal(AnimalEntity animal);
    Set<AnimalDto> uploadAnimals(MultipartFile file) throws IOException, JAXBException;

    Page<AnimalEntity> findAll(Pageable pageable);

    Page<AnimalEntity> getFilteredAndSortedAnimals(Pageable pageable, String type, String category, String sex);
}
