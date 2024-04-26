package org.example.repositories;

import org.example.TestDataUtil;
import org.example.domain.AnimalEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class AnimalJPATests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    public void testFindAll() {
        AnimalEntity animal1 = TestDataUtil.createTestAnimalA();
        animal1.setId(null);
        entityManager.persist(animal1);

        AnimalEntity animal2 = TestDataUtil.createTestAnimalB();
        animal2.setId(null);
        entityManager.persist(animal2);

        entityManager.flush();

        Iterable<AnimalEntity> animals = animalRepository.findAll();

        assertThat(animals).containsExactly(animal1, animal2);
    }

    @Test
    public void testSave() {
        AnimalEntity animal = TestDataUtil.createTestAnimalA();
        animal.setId(null);

        AnimalEntity savedAnimal = animalRepository.save(animal);

        assertThat(savedAnimal).isNotNull();
        assertThat(savedAnimal.getId()).isNotNull();
        assertThat(savedAnimal).isEqualTo(animal);
    }

    @Test
    public void testFindById() {
        AnimalEntity animal = TestDataUtil.createTestAnimalA();
        animal.setId(null);
        entityManager.persist(animal);
        entityManager.flush();

        AnimalEntity foundAnimal = animalRepository.findById(Math.toIntExact(animal.getId())).orElse(null);

        assertThat(foundAnimal).isNotNull();
        assertThat(foundAnimal).isEqualTo(animal);
    }

    @Test
    public void testDelete() {
        AnimalEntity animal = TestDataUtil.createTestAnimalA();
        animal.setId(null);
        entityManager.persist(animal);
        entityManager.flush();

        animalRepository.delete(animal);

        assertThat(animalRepository.findById(Math.toIntExact(animal.getId()))).isEmpty();
    }
}
