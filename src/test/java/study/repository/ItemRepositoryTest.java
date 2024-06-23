package study.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.entity.Item;

/**
 * 새로운 엔티티를 구별하는 방법
 *
 * save() 메서드
 * - 새로운 엔티티면 저장 (persist)
 * - 새로운 엔티티가 아니면 병합 (merge)
 *
 * 새로운 엔티티 판단 기본 전략
 * - 식별자(id) 가 객체일 때 null 로 판단
 * - 식별자(id) 가 자바 기본 타입일 때 0으로 판단
 * - Persistable 인터페이스를 구현해서 판단 로직 변경 가능
 */
@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

//    @Test
//    public void saveWithGeneratedValue() {
//        Item item = new Item();
//        itemRepository.save(item);
//    }

    /**
     * 식별자가 존재하여 merge 가 동작하며
     * select 쿼리 후 insert 쿼리가 동작하여 비효율적이다.
     *
     * merge 사용은 권장하지 않으며,
     * 데이터 변경은 변경 감지 기능 사용을 권장하고,
     * 데이터 저장은 persist 를 사용함을 권장한다.
     */
    @Test
    public void saveNonGeneratedValue() {
        Item item = new Item("A");
        itemRepository.save(item);
    }
}