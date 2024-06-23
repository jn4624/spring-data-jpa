package study.repository.projection;

/**
 * 중첩 구조 Projection
 */
public interface NestedClosedProjection {
    String getUsername();

    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
