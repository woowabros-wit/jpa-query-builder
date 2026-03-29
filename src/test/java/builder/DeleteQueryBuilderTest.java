package builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class DeleteQueryBuilderTest {

    @Test
    void 쿼리_생성() {
        WhereCondition whereCondition = new WhereCondition("age", Operator.LT, "?");
        String sql = new DeleteQueryBuilder()
            .from("users")
            .where(whereCondition)
            .build();

        assertEquals("DELETE FROM users WHERE age < ?", sql);
    }

    @Test
    void WHERE_없으면_예외() {
        DeleteQueryBuilder builder = new DeleteQueryBuilder()
            .from("users");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void 테이블명_없으면_예외() {
        WhereCondition whereCondition = new WhereCondition("age", Operator.LT, "?");
        DeleteQueryBuilder builder = new DeleteQueryBuilder()
            .where(whereCondition);

        assertThrows(IllegalStateException.class, builder::build);
    }
}
