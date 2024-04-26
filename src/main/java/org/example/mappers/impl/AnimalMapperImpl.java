package org.example.mappers.impl;

import org.example.domain.AnimalDto;
import org.example.domain.AnimalEntity;
import org.example.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapperImpl implements Mapper<AnimalEntity, AnimalDto> {
    private ModelMapper modelMapper;

    public AnimalMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AnimalDto mapTo(AnimalEntity animalEntity) {
        return modelMapper.map(animalEntity, AnimalDto.class);
    }

    @Override
    public AnimalEntity mapFrom(AnimalDto animalDto) {
        return modelMapper.map(animalDto, AnimalEntity.class);
    }
}
