package study.repository.custom;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.entity.Member;

import java.util.List;

/**
 * 클래스명 마지막에 Impl postfix 를 붙여주는 규칙은 지켜야 한다.
 * Custom 정의를 했다면 기존 인터페이스에 상속하여 사용해도 되지만
 * 별도의 패키지로 분리하여 클래스로 구현하는 것을 권장한다.
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<Member> findMemberCustom() {
        return entityManager.createQuery("select m from Member m")
                .getResultList();
    }
}
