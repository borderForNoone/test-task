package org.example.services.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.example.domain.AnimalDto;
import org.example.domain.AnimalEntity;
import org.example.repositories.AnimalRepository;
import org.example.services.AnimalCsvRepresentation;
import org.example.services.AnimalService;
import org.example.mappers.jaxb.AnimalList;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;

    @Override
    public AnimalEntity createAnimal(AnimalEntity animal) {
        return animalRepository.save(animal);
    }

    @Override
    public Set<AnimalDto> uploadAnimals(MultipartFile file) throws IOException, JAXBException {
        String fileType = determineFileType(file);

        if ("csv".equalsIgnoreCase(fileType)) {
            return parseCsv(file);
        } else if ("xml".equalsIgnoreCase(fileType)) {
            return parseXml(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    @Override
    public Page<AnimalEntity> findAll(Pageable pageable) {
        return animalRepository.findAll(pageable);
    }

    @Override
    public Page<AnimalEntity> getFilteredAndSortedAnimals(Pageable pageable, String type, String category, String sex) {
        Specification<AnimalEntity> spec = Specification.where(null);

        if (type != null && !type.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category"), category));
        }

        if (sex != null && !sex.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("sex"), sex));
        }

        return animalRepository.findAll(spec, pageable);
    }

    private String determineFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                return fileName.substring(dotIndex + 1).toLowerCase();
            }
        }
        throw new IllegalArgumentException("Unknown file type for file: " + fileName);
    }

    private Set<AnimalDto> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<AnimalCsvRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(AnimalCsvRepresentation.class);
            CsvToBean<AnimalCsvRepresentation> csvToBean =
                    new CsvToBeanBuilder<AnimalCsvRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            return csvToBean.parse()
                    .stream()
                    .filter(csvLine -> isValidCsvLine(csvLine))
                    .map(csvLine -> {
                        int cost = csvLine.getCost();
                        String category;

                        if (cost >= 0 && cost <= 20) {
                            category = "1";
                        } else if (cost >= 21 && cost <= 40) {
                            category = "2";
                        } else if (cost >= 41 && cost <= 60) {
                            category = "3";
                        } else {
                            category = "4";
                        }

                        return new AnimalDto()
                                .setName(csvLine.getName())
                                .setType(csvLine.getType())
                                .setSex(csvLine.getSex())
                                .setWeight(csvLine.getWeight())
                                .setCost(cost)
                                .setCategory(category);
                    }).collect(Collectors.toSet());
        }
    }

    private boolean isValidCsvLine(AnimalCsvRepresentation csvLine) {
        return csvLine.getName() != null &&
                csvLine.getType() != null &&
                csvLine.getSex() != null &&
                csvLine.getWeight() != null &&
                csvLine.getCost() != null &&
                !csvLine.getName().isEmpty() &&
                !csvLine.getType().isEmpty() &&
                !csvLine.getSex().isEmpty();
    }

    private Set<AnimalDto> parseXml(MultipartFile file) throws IOException, JAXBException {
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimalList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AnimalList animalList = (AnimalList) jaxbUnmarshaller.unmarshal(inputStream);

            return animalList.getAnimals().stream()
                    .filter(xmlRepresentation ->
                            xmlRepresentation.getName() != null &&
                                    xmlRepresentation.getType() != null &&
                                    xmlRepresentation.getSex() != null &&
                                    xmlRepresentation.getWeight() != null &&
                                    xmlRepresentation.getCost() != null)
                    .map(xmlRepresentation -> {
                        int cost = xmlRepresentation.getCost().intValue();
                        String category;

                        if (cost >= 0 && cost <= 20) {
                            category = "1";
                        } else if (cost >= 21 && cost <= 40) {
                            category = "2";
                        } else if (cost >= 41 && cost <= 60) {
                            category = "3";
                        } else {
                            category = "4";
                        }

                        return new AnimalDto()
                                .setName(xmlRepresentation.getName())
                                .setType(xmlRepresentation.getType())
                                .setSex(xmlRepresentation.getSex())
                                .setWeight(xmlRepresentation.getWeight())
                                .setCost(cost)
                                .setCategory(category);
                    }).collect(Collectors.toSet());
        }
    }
}
