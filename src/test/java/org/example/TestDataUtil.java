package org.example;

import org.example.domain.AnimalEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;

import java.io.IOException;

public class TestDataUtil {
    public static MockMultipartFile createXMLFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("animals.xml");
        byte[] fileContent = StreamUtils.copyToByteArray(resource.getInputStream());
        return new MockMultipartFile("file", resource.getFilename(), "text/xml", fileContent);
    }

    public static MockMultipartFile createCSVFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("animals.csv");
        byte[] fileContent = StreamUtils.copyToByteArray(resource.getInputStream());
        return new MockMultipartFile("file", resource.getFilename(), "text/csv", fileContent);
    }


    public static AnimalEntity createTestAnimalA() {
        return AnimalEntity.builder()
                .id(1L)
                .name("Milo")
                .type("cat")
                .sex("male")
                .weight(38)
                .cost(59)
                .category("3")
                .build();
    }

    public static AnimalEntity createTestAnimalB() {
        return AnimalEntity.builder()
                .id(2L)
                .name("Simba")
                .type("dog")
                .sex("male")
                .weight(14)
                .cost(14)
                .category("1")
                .build();
    }

    public static AnimalEntity createTestAnimalC() {
        return AnimalEntity.builder()
                .id(3L)
                .name("Lola")
                .type("dog")
                .sex("male")
                .weight(35)
                .cost(105)
                .category("4")
                .build();
    }
}
