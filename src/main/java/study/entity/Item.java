package study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {
//    @Id
//    @GeneratedValue
//    private Long id;

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    protected Item() {
    }

    public Item(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
