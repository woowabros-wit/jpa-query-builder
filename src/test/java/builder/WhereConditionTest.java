package builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class WhereConditionTest {

    @Test
    void IS_NULL_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("name", Operator.IS_NULL);
        assertEquals("name IS NULL", condition.generateWhereConditionString());
    }

    @Test
    void IS_NOT_NULL_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("email", Operator.IS_NOT_NULL);
        assertEquals("email IS NOT NULL", condition.generateWhereConditionString());
    }

    @Test
    void EQ_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("age", Operator.EQ, "30");
        assertEquals("age = 30", condition.generateWhereConditionString());
    }

    @Test
    void NE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("status", Operator.NE, "'active'");
        assertEquals("status != 'active'", condition.generateWhereConditionString());
    }

    @Test
    void GT_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("price", Operator.GT, "100");
        assertEquals("price > 100", condition.generateWhereConditionString());
    }

    @Test
    void LT_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("quantity", Operator.LT, "50");
        assertEquals("quantity < 50", condition.generateWhereConditionString());
    }

    @Test
    void GTE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("rating", Operator.GTE, "4.5");
        assertEquals("rating >= 4.5", condition.generateWhereConditionString());
    }

    @Test
    void LTE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("discount", Operator.LTE, "20");
        assertEquals("discount <= 20", condition.generateWhereConditionString());
    }

    @Test
    void LIKE_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("name", Operator.LIKE, "%John%");
        assertEquals("name LIKE '%John%'", condition.generateWhereConditionString());
    }

    @Test
    void BETWEEN_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("price", Operator.BETWEEN, "100", "200");
        assertEquals("price BETWEEN 100 AND 200", condition.generateWhereConditionString());
    }

    @Test
    void IN_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("category", Operator.IN, "'electronics'", "'books'", "'clothing'");
        assertEquals("category IN ('electronics', 'books', 'clothing')", condition.generateWhereConditionString());
    }

    @Test
    void NOT_IN_연산자_sql_생성() {
        WhereCondition condition = new WhereCondition("status", Operator.NOT_IN, "'inactive'", "'banned'");
        assertEquals("status NOT IN ('inactive', 'banned')", condition.generateWhereConditionString());
    }

    @Test
    void 잘못된_값_개수로_생성하면_예외_발생() {
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("name", Operator.EQ));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("price", Operator.BETWEEN, "100"));
        assertThrows(IllegalArgumentException.class, () -> new WhereCondition("category", Operator.IN));
    }
}
