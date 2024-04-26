package org.example.domain;

import java.util.Objects;

public class AnimalDto {
    private Long id;
    private String name;
    private String type;
    private String sex;
    private Integer weight;
    private Integer cost;
    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AnimalDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public AnimalDto setType(String type) {
        this.type = type;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public AnimalDto setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }

    public AnimalDto setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public AnimalDto setCost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public AnimalDto setCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public String toString() {
        return "AnimalDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", sex='" + sex + '\'' +
                ", weight=" + weight +
                ", cost=" + cost +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalDto animalDto = (AnimalDto) o;
        return Objects.equals(id, animalDto.id) && Objects.equals(name, animalDto.name) && Objects.equals(type, animalDto.type) && Objects.equals(sex, animalDto.sex) && Objects.equals(weight, animalDto.weight) && Objects.equals(cost, animalDto.cost) && Objects.equals(category, animalDto.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, sex, weight, cost, category);
    }
}
