package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@Entity
@Table(name = "animals")
public class AnimalEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    private String sex;
    private Integer weight;
    private Integer cost;
    private String category;
}
