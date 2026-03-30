package builder;

import static builder.where.ComparisonOperator.EQ;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import builder.where.WhereCondition;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class UpdateQueryBuilderTest {

    @Test
    void 쿼리_생성() {
        String sql = new UpdateQueryBuilder()
            .table("users")
            .set("name", "?")
            .set("age", "?")
            .where(new WhereCondition("id", EQ, "?"))
            .build();

        assertEquals("UPDATE users SET name = ?, age = ? WHERE id = ?", sql);
    }

    @Test
    void WHERE_없으면_예외() {
        UpdateQueryBuilder builder = new UpdateQueryBuilder()
            .table("users")
            .set("name", "?");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void 테이블명_없으면_예외() {
        UpdateQueryBuilder builder = new UpdateQueryBuilder()
            .set("name", "?")
            .where(new WhereCondition("id", EQ, "?"));

        assertThrows(IllegalStateException.class, builder::build);
    }
}
