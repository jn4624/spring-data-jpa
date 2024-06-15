package study.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.dto.MemberDTO;
import study.entity.Member;
import study.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void testMember() {
        Member member = new Member("memberA");

        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // JPA 엔티티 동일성 보장
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();

        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();

        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();

        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
    }

    @Test
    void testQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findMember("AAA", 10);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void findUsernameList() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();

        for (String s : result) {
            System.out.println("s = " + s);
        }

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("AAA", 10, team);
        memberRepository.save(member);

        List<MemberDTO> result = memberRepository.findMemberDto();

        for (MemberDTO dto : result) {
            System.out.println("dto = " + dto);
        }

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getTeamName()).isEqualTo("teamA");
        assertThat(result.get(0).getId()).isEqualTo(1);
    }

    @Test
    void findByNames() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void returnType() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findListMember = memberRepository.findListByUsername("AAA");
        System.out.println("findListMember = " + findListMember);

        Member findMember = memberRepository.findMemberByUsername("BBB");
        System.out.println("findMember = " + findMember);

        Optional<Member> findOptionalMember = memberRepository.findOptionalByUsername("AAA");
        System.out.println("findOptionalMember = " + findOptionalMember.get());

        assertThat(findListMember.size()).isEqualTo(1);
        assertThat(findMember).isNotNull();
        assertThat(findOptionalMember).isNotNull();
    }

    @Test
    void page() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // when
        PageRequest pageRequest = PageRequest
                .of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findPageByAge(10, pageRequest);

        // 페이지 계산 공식 적용 부분

        // then
        List<Member> content = page.getContent(); // 조회된 데이터
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가?
    }

    @Test
    void slice() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> page = memberRepository.findSliceByAge(10, pageRequest);

        // 페이지 계산 공식 적용 부분

        // then
        List<Member> content = page.getContent(); // 조회된 데이터

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가?
    }

    @Test
    void countQuery() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> members = memberRepository.findMemberAllCountBy(pageRequest);
    }

    @Test
    void entityToDto() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> members = memberRepository.findMemberAllCountBy(pageRequest);
        Page<MemberDTO> map = members.map(m -> new MemberDTO(m.getId(), m.getUsername(), null));

        System.out.println(map.getContent());
    }

    @Test
    void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        /**
         * 벌크 연산은 영속성 컨텍스트를 무시하고
         * 데이터베이스에 바로 반영을 실행하기 때문에
         * 영속성 컨텍스트에 있는 엔티티와 데이터베이스간의 데이터가 달라진다.
         *
         * 벌크 연산 사용의 권장 방안은
         * 1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
         * 2. 부득이하게 영속성 컨텍스트에 엔티티가 존재하면 벌크 연산 직후 영속성 컨텍스트를 초기화한다.
         */
        int resultCount = memberRepository.bulkAgePlus(20);

        // 영속성 컨텍스트 초기화
        /**
         * Spring Data JPA 는
         * EntityManager 로 초기화를 할 수도 있지만
         * @Modifiying 의 clearAutomatically 옵션으로도 초기화를 지정할 수 있다.
         */
//        entityManager.flush();
//        entityManager.clear();

        List<Member> members = memberRepository.findByUsername("member5");
        Member member5 = members.get(0);

        // then
        assertThat(resultCount).isEqualTo(3);
        assertThat(member5.getAge()).isEqualTo(41);
    }

    @Test
    void findMemberLazy() {
        // given
        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        entityManager.flush();
        entityManager.clear();

        // when
        List<Member> members = memberRepository.findAll();

        // then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    void findMemberFetchJoin() {
        // given
        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        entityManager.flush();
        entityManager.clear();

        // when
        List<Member> members = memberRepository.findMemberFetchJoin();

        // then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    void findEntityGraphByUsername() {
        // given
        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        entityManager.flush();
        entityManager.clear();

        // when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        // then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    void queryHint() {
        Member member = memberRepository.save(new Member("member1", 10));

        entityManager.flush();
        entityManager.clear();

        Member findMember1 = memberRepository.findReadOnlyByUsername(member.getUsername());
        findMember1.setUsername("member2");

        entityManager.flush();
    }

    @Test
    void lock() {
        Member member = memberRepository.save(new Member("member1", 10));

        entityManager.flush();
        entityManager.clear();

        List<Member> members = memberRepository.findLockByUsername("member1");
    }

    @Test
    void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }
}
