package study.repository.projection;

/**
 * 클래스 기반 Projection
 */
public class UsernameOnlyDTO {
    private final String username;

    public UsernameOnlyDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
