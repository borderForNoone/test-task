package org.example.mappers.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.example.domain.AnimalDto;

import java.util.List;

@XmlRootElement(name = "animals")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimalList {
    @XmlElement(name = "animal")
    private List<AnimalDto> animals;

    public List<AnimalDto> getAnimals() {
        return animals;
    }

    public void setAnimals(List<AnimalDto> animals) {
        this.animals = animals;
    }
}
