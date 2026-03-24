package jdbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectQueryBuilderTest {

    @Test
    void 기본_SELECT_쿼리_생성() {
        String sql = new SelectQueryBuilder()
                .select("id", "name")
                .from("users")
                .build();

        assertEquals("SELECT id, name FROM users", sql);
    }

    @Test
    void SELECT_없이_호출하면_기본값_사용() {
        String sql = new SelectQueryBuilder()
                .from("users")
                .build();

        assertEquals("SELECT * FROM users", sql);
    }

    @Test
    void FROM_없이_빌드하면_예외_발생() {
        SelectQueryBuilder builder = new SelectQueryBuilder()
                .select("*");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }
}