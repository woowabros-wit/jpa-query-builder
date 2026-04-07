package jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.DataBaseTestSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ResultSetMapperTest extends DataBaseTestSupport {

    private ResultSetMapper resultSetMapper = new ResultSetMapper();

    @BeforeEach
    void setUp() {
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
    void mapToObject() throws Exception {
        // given
        final String name = "name1";
        final int age = 31;
        final String email = "name1@example.com";

        insertUser(name, age, email);
        final String sql = "SELECT * FROM users limit 1";
        executeQuery(sql, rs -> {
            // when
            final User user = resultSetMapper.mapToObject(rs, User.class);

            // then
            System.out.println("================================================");
            System.out.println("================================================");
            System.out.println("================================================");
            System.out.println("user = " + user);
            System.out.println("================================================");
            System.out.println("================================================");
            System.out.println("================================================");

            assertSoftly(assertions -> {
                assertions.assertThat(user.getId()).isNotNull();
                assertions.assertThat(user.getName()).isEqualTo(name);
                assertions.assertThat(user.getAge()).isEqualTo(age);
                assertions.assertThat(user.getEmail()).isEqualTo(email);
                assertions.assertThat(user.getCreatedAt()).isNotNull();
            });
        });
    }

    @DisplayName("mapToObject 컬럼타입과 필드타입이 호환되지 않는 경우 에러")
    @Test
    void mapToObject1() throws Exception {
        // given
        final String name = "name1";
        final int age = 31;
        final String email = "name1@example.com";
        insertUser(name, age, email);
        final String sql = "SELECT * FROM users limit 1";

        executeQuery(sql, rs -> {
            // when
            assertThatThrownBy(() -> resultSetMapper.mapToObject(rs, InvalidUser.class))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("지원하지 않는 JDBC 타입입니다. jdbcType: [BIGINT], fieldType: [java.lang.Integer]");
        });
    }

    @Test
    void mapToList() throws Exception {
        // given
        insertUser("name1", 31, "name1@example.com");
        insertUser("name2", 32, "name2@example.com");

        final String sql = "SELECT * FROM users";
        executeQuery(sql, rs -> {
            // when
            resultSetMapper.mapToList(rs, User.class);
        });

    }

    private void executeQuery(String sql, Consumer<ResultSet> resultSetConsumer) throws Exception{
        final Connection connection = getConnection();
        try(final PreparedStatement statement = connection.prepareStatement(sql)) {
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new IllegalStateException("데이터가 존재하지 않습니다.");
            }
            resultSetConsumer.accept(resultSet);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
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

    private static class InvalidUser {
        private Integer id;
        private String name;
    }


}