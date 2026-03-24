package persistence;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InsertQueryBuilderTest {
    /**
     * INSERT 쿼리를 생성하여 새로운 데이터를 데이터베이스에 추가할 수 있다.
     *   e.g. INSERT INTO users (name, age, email) VALUES (?, ?, ?)
     *
     * INSERT 쿼리는 최소 1개 이상의 컬럼-값 쌍을 가져야 하며, 없으면 예외를 발생시킨다.
     *
     * 모든 쿼리에서 파라미터 플레이스홀더(?)를 사용하여 SQL 인젝션을 방지한다.
     */


    @Test
    void case1() {
        String sql = new InsertQueryBuilder()
                .into("users")
                .value("name", "?")
                .value("age", "?")
                .value("email", "?")
                .build();

        assertThat(sql).isEqualTo("INSERT INTO users (name, age, email) VALUES (?, ?, ?)");
    }


    @Test
    void case2() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("name", "?");
        values.put("age", "?");

        String sql = new InsertQueryBuilder()
                .into("users")
                .values(values)
                .build();

        assertThat(sql).isEqualTo("INSERT INTO users (name, age) VALUES (?, ?)");
    }
}