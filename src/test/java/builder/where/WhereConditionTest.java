package builder.where;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class WhereConditionTest {

    @Test
    void IS_NULL_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("name", ComparisonOperator.IS_NULL);
        assertEquals("name IS NULL", condition.generateWhereConditionString());
    }

    @Test
    void IS_NOT_NULL_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("email", ComparisonOperator.IS_NOT_NULL);
        assertEquals("email IS NOT NULL", condition.generateWhereConditionString());
    }

    @Test
    void EQ_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("age", ComparisonOperator.EQ, "30");
        assertEquals("age = 30", condition.generateWhereConditionString());
    }

    @Test
    void NE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("status", ComparisonOperator.NE, "'active'");
        assertEquals("status != 'active'", condition.generateWhereConditionString());
    }

    @Test
    void GT_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("price", ComparisonOperator.GT, "100");
        assertEquals("price > 100", condition.generateWhereConditionString());
    }

    @Test
    void LT_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("quantity", ComparisonOperator.LT, "50");
        assertEquals("quantity < 50", condition.generateWhereConditionString());
    }

    @Test
    void GTE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("rating", ComparisonOperator.GTE, "4.5");
        assertEquals("rating >= 4.5", condition.generateWhereConditionString());
    }

    @Test
    void LTE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("discount", ComparisonOperator.LTE, "20");
        assertEquals("discount <= 20", condition.generateWhereConditionString());
    }

    @Test
    void LIKE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("name", ComparisonOperator.LIKE, "%John%");
        assertEquals("name LIKE '%John%'", condition.generateWhereConditionString());
    }

    @Test
    void BETWEEN_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("price", ComparisonOperator.BETWEEN, "100", "200");
        assertEquals("price BETWEEN 100 AND 200", condition.generateWhereConditionString());
    }

    @Test
    void IN_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("category", ComparisonOperator.IN, "'electronics'", "'books'", "'clothing'");
        assertEquals("category IN ('electronics', 'books', 'clothing')", condition.generateWhereConditionString());
    }

    @Test
    void NOT_IN_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("status", ComparisonOperator.NOT_IN, "'inactive'", "'banned'");
        assertEquals("status NOT IN ('inactive', 'banned')", condition.generateWhereConditionString());
    }

    @Test
    void 컬럼명이_null이면_예외_발생() {
        assertThrows(IllegalStateException.class, () -> new WhereCondition(null, ComparisonOperator.EQ, "value"));
    }

    @Test
    void 컬럼명이_빈_문자열이면_예외_발생() {
        assertThrows(IllegalStateException.class, () -> new WhereCondition("", ComparisonOperator.EQ, "value"));
    }

    @Test
    void 컬럼명이_공백만_있으면_예외_발생() {
        assertThrows(IllegalStateException.class, () -> new WhereCondition("   ", ComparisonOperator.EQ, "value"));
    }

    @Test
    void IS_NULL_연산자는_값이_0개_필요() {
        assertDoesNotThrow(() -> new WhereCondition("name", ComparisonOperator.IS_NULL));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("name", ComparisonOperator.IS_NULL, "value"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("name", ComparisonOperator.IS_NULL, "value1", "value2"));
    }

    @Test
    void IS_NOT_NULL_연산자는_값이_0개_필요() {
        assertDoesNotThrow(() -> new WhereCondition("email", ComparisonOperator.IS_NOT_NULL));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("email", ComparisonOperator.IS_NOT_NULL, "value"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("email", ComparisonOperator.IS_NOT_NULL, "value1", "value2"));
    }

    @Test
    void EQ_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("age", ComparisonOperator.EQ));
        assertDoesNotThrow(() -> new WhereCondition("email", ComparisonOperator.EQ, "10"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("age", ComparisonOperator.EQ, "30", "40"));
    }

    @Test
    void NE_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("status", ComparisonOperator.NE));
        assertDoesNotThrow(() -> new WhereCondition("status", ComparisonOperator.NE, "active"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("status", ComparisonOperator.NE, "active", "inactive"));
    }

    @Test
    void GT_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("price", ComparisonOperator.GT));
        assertDoesNotThrow(() -> new WhereCondition("price", ComparisonOperator.GT, "100"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("price", ComparisonOperator.GT, "100", "200"));
    }

    @Test
    void LT_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("quantity", ComparisonOperator.LT));
        assertDoesNotThrow(() -> new WhereCondition("quantity", ComparisonOperator.LT, "50"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("quantity", ComparisonOperator.LT, "50", "100"));
    }

    @Test
    void GTE_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("rating", ComparisonOperator.GTE));
        assertDoesNotThrow(() -> new WhereCondition("rating", ComparisonOperator.GTE, "4.5"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("rating", ComparisonOperator.GTE, "4.5", "5.0"));
    }

    @Test
    void LTE_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("discount", ComparisonOperator.LTE));
        assertDoesNotThrow(() -> new WhereCondition("discount", ComparisonOperator.LTE, "20"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("discount", ComparisonOperator.LTE, "20", "30"));
    }

    @Test
    void LIKE_연산자는_정확히_1개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("name", ComparisonOperator.LIKE));
        assertDoesNotThrow(() -> new WhereCondition("name", ComparisonOperator.LIKE, "%John%"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("name", ComparisonOperator.LIKE, "%John%", "%Jane%"));
    }

    @Test
    void BETWEEN_연산자는_정확히_2개의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("price", ComparisonOperator.BETWEEN, "100"));
        assertDoesNotThrow(() -> new WhereCondition("price", ComparisonOperator.BETWEEN, "100", "200"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("price", ComparisonOperator.BETWEEN, "100", "200", "300"));
    }

    @Test
    void IN_연산자는_최소_1개_이상의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("category", ComparisonOperator.IN));
        assertDoesNotThrow(() -> new WhereCondition("category", ComparisonOperator.IN, "'electronics'"));
        assertDoesNotThrow(() -> new WhereCondition("category", ComparisonOperator.IN, "'electronics'", "'books'", "'clothing'"));
    }

    @Test
    void NOT_IN_연산자는_최소_1개_이상의_값_필요() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("status", ComparisonOperator.NOT_IN));
        assertDoesNotThrow(() -> new WhereCondition("status", ComparisonOperator.NOT_IN, "'inactive'"));
        assertDoesNotThrow(() -> new WhereCondition("status", ComparisonOperator.NOT_IN, "'inactive'", "'banned'"));
    }
}
