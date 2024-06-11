package study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
