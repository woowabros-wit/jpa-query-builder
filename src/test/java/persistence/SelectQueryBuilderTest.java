package persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SelectQueryBuilderTest {

    @Test
    void case1() {
        // 예시 1: 기본 조회
        String sql = new SelectQueryBuilder()
                .select("id", "name", "age")
                .from("users")
                .build();

        assertThat(sql).isEqualTo("SELECT id, name, age FROM users");
    }

    @Test
    void case2() {
        // 예시 2: 정렬 추가
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .orderBy("age", "DESC")
                .build();

        assertThat(sql).isEqualTo("SELECT * FROM users ORDER BY age DESC");
    }


    @Test
    void case3() {
        // 예시 3: LIMIT 추가
        String sql = new SelectQueryBuilder()
                .select("id", "name")
                .from("users")
                .orderBy("id", "ASC")
                .limit(10)
                .build();

        assertThat(sql).isEqualTo("SELECT id, name FROM users ORDER BY id ASC LIMIT 10");

    }

    @Test
    void case4() {
        // 특정 컬럼을 지정하지 않으면 자동으로 전체 컬럼(*)을 선택한다.
        // 예시 1: 기본 조회
        String sql = new SelectQueryBuilder()
                .select()
                .from("users")
                .build();

        assertThat(sql).isEqualTo("SELECT * FROM users");
    }

    @Test
    void case5() {
        // 메서드 호출 순서는 자유롭게 지정할 수 있다.
        //e.g. select().from().orderBy() 또는 from().orderBy().select() 모두 동일한 결과

        String sql1 = new SelectQueryBuilder()
                .select()
                .from("users")
                .orderBy("id", "ASC")
                .build();

        String sql2 = new SelectQueryBuilder()
                .from("users")
                .orderBy("id", "ASC")
                .select()
                .build();

        assertThat(sql1).isEqualTo("SELECT * FROM users ORDER BY id ASC");
        assertThat(sql1).isEqualTo(sql2);
    }
}