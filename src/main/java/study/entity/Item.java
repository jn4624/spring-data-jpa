package study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Item {
//    @Id
//    @GeneratedValue
//    private Long id;

    @Id
    private String id;

    protected Item() {
    }

    public Item(String id) {
        this.id = id;
    }
}
