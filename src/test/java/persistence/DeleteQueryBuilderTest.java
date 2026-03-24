package persistence;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteQueryBuilderTest {

    @Test
    void case1() {
        String sql = new DeleteQueryBuilder()
                .from("users")
                .where("age < ?")
                .build();

        assertThat(sql).isEqualTo("DELETE FROM users WHERE age < ?");
    }
}