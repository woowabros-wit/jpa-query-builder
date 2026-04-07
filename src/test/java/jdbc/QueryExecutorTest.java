package jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import query.NamedParameterQuery;
import support.DataBaseTestSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class QueryExecutorTest extends DataBaseTestSupport {

    private QueryExecutor queryExecutor;

    @BeforeEach
    void setUp() {
        queryExecutor = new QueryExecutor(getConnection());
        createUserTable();
    }

    private void createUserTable() {
        final String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                age INT NOT NULL,
                email VARCHAR(255) NOT NULL,
                created_at DATETIME(6) DEFAULT NOW(6)
            )
            """;
        final Connection connection = getConnection();
        try (final Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterEach
    void tearDown() {
        truncateTable("users");
    }

    @Test
    void query() throws Exception {
        // given
        final String sql = "SELECT * FROM users WHERE age < ?";
        insertUser("name1", 31, "name1@example.com");
        insertUser("name2", 32, "name2@example.com");
        insertUser("name3", 33, "name3@example.com");


        // when
        final List<User> result = queryExecutor.query(sql, User.class, 33);

        // then
        assertThat(result).hasSize(2);
        assertSoftly(assertions -> {
            final User firstUser = result.get(0);
            assertions.assertThat(firstUser.getName()).isEqualTo("name1");
            assertions.assertThat(firstUser.getAge()).isEqualTo(31);
            assertions.assertThat(firstUser.getEmail()).isEqualTo("name1@example.com");

            final User secondUser = result.get(1);
            assertions.assertThat(secondUser.getName()).isEqualTo("name2");
            assertions.assertThat(secondUser.getAge()).isEqualTo(32);
            assertions.assertThat(secondUser.getEmail()).isEqualTo("name2@example.com");
        });
    }

    @DisplayName("query - NamedParameterQuery 를 사용한 쿼리 실행")
    @Test
    void query1() throws Exception {
        // given
        final String sql = "SELECT * FROM users WHERE age < :age";
        insertUser("name1", 31, "name1@example.com");
        insertUser("name2", 32, "name2@example.com");
        insertUser("name3", 33, "name3@example.com");

        final NamedParameterQuery namedParameterQuery = new NamedParameterQuery(sql)
                .setParameter("age", 33);

        // when
        final List<User> result = queryExecutor.query(namedParameterQuery, User.class);

        // then
        assertThat(result).hasSize(2);
        assertSoftly(assertions -> {
            final User firstUser = result.get(0);
            assertions.assertThat(firstUser.getName()).isEqualTo("name1");
            assertions.assertThat(firstUser.getAge()).isEqualTo(31);
            assertions.assertThat(firstUser.getEmail()).isEqualTo("name1@example.com");

            final User secondUser = result.get(1);
            assertions.assertThat(secondUser.getName()).isEqualTo("name2");
            assertions.assertThat(secondUser.getAge()).isEqualTo(32);
            assertions.assertThat(secondUser.getEmail()).isEqualTo("name2@example.com");
        });
    }

    @Test
    void execute() throws Exception {
        // given
        insertUser("name1", 31, "name1@example.com");
        insertUser("name2", 32, "name2@example.com");
        insertUser("name3", 33, "name3@example.com");

        final String sql = "UPDATE users SET age = ? WHERE name = ?";

        // when
        queryExecutor.execute(sql, 35, "name1");

        // then
        final String selectSql = "SELECT * FROM users WHERE name = ?";
        final List<User> result = queryExecutor.query(selectSql, User.class, "name1");

        assertThat(result).hasSize(1);
        final User user = result.getFirst();
        assertThat(user.getAge()).isEqualTo(35);
    }

    @DisplayName("execute - NamedParameterQuery 를 사용한 쿼리 실행")
    @Test
    void execute1() throws Exception {
        // given
        insertUser("name1", 31, "name1@example.com");
        insertUser("name2", 32, "name2@example.com");
        insertUser("name3", 33, "name3@example.com");

        final String sql = "UPDATE users SET age = :age WHERE name = :name";
        final NamedParameterQuery namedParameterQuery = new NamedParameterQuery(sql)
                .setParameter("age", 35)
                .setParameter("name", "name1");

        // when
        queryExecutor.execute(namedParameterQuery);

        // then
        final String selectSql = "SELECT * FROM users WHERE name = ?";
        final List<User> result = queryExecutor.query(selectSql, User.class, "name1");

        assertThat(result).hasSize(1);
        final User user = result.getFirst();
        assertThat(user.getAge()).isEqualTo(35);
    }

    private void insertUser(String name, int age, String email) {
        final String sql = """
            INSERT INTO users (name, age, email) VALUES (?, ?, ?)
            """;
        final Connection connection = getConnection();
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class User {
        private Long id;
        private String name;
        private Integer age;
        private String email;
        private LocalDateTime createdAt;

        public User() {}

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", email='" + email + '\'' +
                    ", createdAt=" + createdAt +
                    '}';
        }

    }
}