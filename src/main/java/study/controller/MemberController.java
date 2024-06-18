package study.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.dto.MemberDTO;
import study.entity.Member;
import study.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 주의:
     * 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면,
     * 이 엔티티는 단순 조회용으로만 사용해야 한다.
     * (트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 데이터베이스에 반영되지 않는다.)
     */
    @GetMapping("/members/{id}/converter")
    public String findMemberConverter(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * ex:
     * http://localhost:8080/members?page=0&size=3&sort=id,desc&sort=createdDate,asc
     */
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * 페이징과 정렬 개별 설정
     * 글로벌 설정보다 우선 순위가 높다.
     */
    @GetMapping("/members/default")
    public Page<Member> listDefault(@PageableDefault(
            size = 12,
            sort = "username",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * Page To DTO
     */
    @GetMapping("/members/dto")
    public Page<MemberDTO> listDto(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDTO::new);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
