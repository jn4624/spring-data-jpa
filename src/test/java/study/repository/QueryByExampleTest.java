package study.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;
import study.entity.Member;
import study.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QueryByExampleTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    /**
     * Query By Example
     *
     * 장점
     * - 동적 쿼리를 편리하게 처리
     * - 도메인 객체를 그대로 사용
     * - 데이터 저장소를 RDB 에서 NoSQL 로 변경해도 코드 변경 없게 추상화 되어 있음
     *
     * 단점
     * - 조인은 가능하지만 내부 조인만 가능함 (외부 조인 불가능)
     * - 중첩 제약 조건 불가능
     * - 매칭 조건의 단순함
     */
    @Test
    void basic() {
        // given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        entityManager.persist(new Member("m1", 0, teamA));
        entityManager.persist(new Member("m2", 0, teamA));

        entityManager.flush();
        entityManager.clear();

        // when
        // Probe 생성
        Member member = new Member("m1");
        Team team = new Team("teamA"); // 내부 조인으로 teamA 가능
        member.setTeam(team);

        // ExampleMatcher 생성, age 프로퍼티는 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        // then
        assertThat(result.get(0).getUsername()).isEqualTo("m1");
        assertThat(result.size()).isEqualTo(1);
    }
}
