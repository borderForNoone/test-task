package org.example.services;

import jakarta.xml.bind.JAXBException;
import org.example.TestDataUtil;
import org.example.domain.AnimalDto;
import org.example.domain.AnimalEntity;
import org.example.repositories.AnimalRepository;
import org.example.services.impl.AnimalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceImplTest {
    @Mock
    private AnimalRepository animalRepository;
    @InjectMocks
    private AnimalServiceImpl underTest;

    @Test
    public void testThatListAnimalsReturnsFromRepository() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<AnimalEntity> findAllResult =
                new PageImpl<>(List.of(TestDataUtil.createTestAnimalA()), pageable, 1);

        when(animalRepository.findAll(eq(pageable))).thenReturn(findAllResult);
        final Page<AnimalEntity> listAnimalsResult = underTest.findAll(pageable);
        assertThat(listAnimalsResult).isEqualTo(findAllResult);
    }

    @Test
    public void testUploadAnimalsThrowsExceptionForInvalidFileType() {
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("invalid.ext");

        assertThrows(IllegalArgumentException.class, () -> underTest.uploadAnimals(mockFile));
    }

    @Test
    public void testParseCsvSkipsInvalidLines() throws IOException, JAXBException {
        MockMultipartFile file = TestDataUtil.createCSVFile();

        Set<AnimalDto> animalDtos = underTest.uploadAnimals(file);

        assertThat(animalDtos).hasSize(7);

        AnimalDto animal = animalDtos.stream().filter(dto -> dto.getName().equals("Lola")).findFirst().get();
        assertThat(animal.getCategory()).isEqualTo("4");
        assertThat(animal.getCost()).isEqualTo(105);
    }

    @Test
    public void testParsXMLSkipsInvalidLines() throws IOException, JAXBException {
        MockMultipartFile file = TestDataUtil.createXMLFile();

        Set<AnimalDto> animalDtos = underTest.uploadAnimals(file);

        assertThat(animalDtos).hasSize(7);

        AnimalDto animal = animalDtos.stream().filter(dto -> dto.getName().equals("Simba")).findFirst().get();
        assertThat(animal.getCategory()).isEqualTo("3");
        assertThat(animal.getCost()).isEqualTo(57);
    }

    @Test
    public void testGetFilteredAndSortedAnimals_appliesFilters() {
        Pageable pageable = PageRequest.of(0, 7, Sort.by(Sort.Direction.DESC, "id"));
        String type = null;
        String category = null;
        String sex = "male";

        List<AnimalEntity> animals = Arrays.asList(
                TestDataUtil.createTestAnimalA(),
                TestDataUtil.createTestAnimalB(),
                TestDataUtil.createTestAnimalC()
        );

        Page<AnimalEntity> expectedResult = new PageImpl<>(animals, pageable, animals.size());
        when(animalRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedResult);

        Page<AnimalEntity> actualResult = underTest.getFilteredAndSortedAnimals(pageable, type, category, sex);

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
