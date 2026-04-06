# jpa-query-builder

## 기능 요구 사항

- 문자열 결합 없이 SQL SELECT 쿼리를 프로그래밍 방식으로 안전하게 생성하는 빌더 클래스를 구현한다.
- 메서드 체이닝을 통해 쿼리의 각 구성 요소를 순차적으로 조립하고, 최종적으로 실행 가능한 SQL 문자열을 반환한다.
- 조회할 컬럼, 테이블, 정렬 순서, 조회 개수를 지정할 수 있다.
    - e.g. `SELECT id, name FROM users ORDER BY age DESC LIMIT 10`
- 특정 컬럼을 지정하지 않으면 자동으로 전체 컬럼(`*`)을 선택한다.
- 메서드 호출 순서는 자유롭게 지정할 수 있다.
    - e.g. `select().from().orderBy()` 또는 `from().orderBy().select()` 모두 동일한 결과
- 필수 메서드(테이블 지정)를 호출하지 않으면 빌드 시 명확한 예외를 발생시킨다.
- 생성된 SQL은 JDBC의 `PreparedStatement`에서 직접 사용 가능한 형식이다.
- 테이블 지정(`from`)은 반드시 호출해야 하며, 호출하지 않으면 `IllegalStateException`을 발생시킨다.
- 조회 개수 제한(`limit`)은 양의 정수만 허용하며, 0 이하 값은 `IllegalArgumentException`을 발생시킨다.
- 동일한 메서드를 여러 번 호출하면 마지막 호출 값으로 덮어쓴다.
    - e.g. `.select("id").select("name")` → 최종 결과는 `SELECT name`
- 이번 단계에서는 단일 컬럼 정렬만 지원하며, WHERE 조건은 지원하지 않는다.
- JOIN, GROUP BY, HAVING 등의 고급 기능은 이번 과제에서 제외한다.


**Fluent API 패턴을 활용한 SELECT 쿼리 빌더 구현**

- [ ] SelectQueryBuilder 인터페이스 설계


## 기능 요구 사항

- 데이터 삽입(INSERT), 수정(UPDATE), 삭제(DELETE)를 위한 쿼리 빌더를 구현하고, 1단계의 SELECT 빌더에 WHERE 조건 기능을 추가한다.
- INSERT 쿼리를 생성하여 새로운 데이터를 데이터베이스에 추가할 수 있다.
    - e.g. `INSERT INTO users (name, age, email) VALUES (?, ?, ?)`
    - 컬럼과 값을 하나씩 추가하거나, Map으로 한 번에 추가할 수 있다.
- UPDATE 쿼리를 생성하여 기존 데이터를 수정할 수 있다.
    - e.g. `UPDATE users SET name = ?, age = ? WHERE id = ?`
    - SET 절에 여러 컬럼을 지정할 수 있다.
    - WHERE 조건을 통해 수정 대상을 특정할 수 있다.
- DELETE 쿼리를 생성하여 데이터를 삭제할 수 있다.
    - e.g. `DELETE FROM users WHERE age < ?`
    - WHERE 조건을 통해 삭제 대상을 특정할 수 있다.
- SELECT 쿼리에 WHERE 조건을 추가하여 특정 조건의 데이터만 조회할 수 있다.
    - e.g. `SELECT * FROM users WHERE age >= ? AND status = ?`
- 모든 쿼리에서 파라미터 플레이스홀더(`?`)를 사용하여 SQL 인젝션을 방지한다.
- INSERT 쿼리는 최소 1개 이상의 컬럼-값 쌍을 가져야 하며, 없으면 예외를 발생시킨다.
- UPDATE와 DELETE 쿼리는 WHERE 조건을 필수로 요구한다.
    - WHERE 없는 UPDATE/DELETE는 위험하므로 빌드 시 예외를 발생시킨다.
    - e.g. `UPDATE users SET name = ?` → 예외 발생 (WHERE 없음)
- 파라미터 값은 직접 쿼리 문자열에 포함하지 않고, 플레이스홀더(`?`)만 사용한다.
    - e.g. `"name = ?"` (O), `"name = 'John'"` (X)
- WHERE 조건은 문자열 형태로 전달하며, AND/OR 조합은 이번 단계에서 제외한다.
    - e.g. `where("age >= ? AND status = ?")` 형태로 한 번에 전달
- LinkedHashMap을 사용하여 컬럼 순서를 보장한다.
    - INSERT/UPDATE 시 컬럼 순서가 추가한 순서대로 유지된다.

Todo

[ ] 

---

- - ## 기능 요구 사항

- 데이터베이스 조회 결과(ResultSet)를 Java 객체로 자동 변환하는 매핑 시스템을 구현하고, 쿼리에서 Named Parameter를 사용할 수 있도록 지원한다.
- ResultSet의 각 행을 Reflection을 사용해 Java 객체로 자동 변환한다.
    - e.g. ResultSet(id=1, name="John", age=30) → User 객체
    - 수동 매핑 코드 없이 자동으로 필드에 값을 설정한다.
- 데이터베이스 컬럼명(snake_case)을 Java 필드명(camelCase)으로 자동 변환한다.
    - e.g. DB 컬럼 `created_at` → Java 필드 `createdAt`
    - e.g. DB 컬럼 `user_name` → Java 필드 `userName`
- JDBC 타입을 Java 타입으로 자동 변환한다.
    - e.g. SQL INTEGER → Java int/Integer/long/Long
    - e.g. SQL VARCHAR → Java String
    - e.g. SQL TIMESTAMP → Java LocalDateTime
    - e.g. SQL BOOLEAN → Java boolean/Boolean
- Named Parameter(`:paramName`) 형식의 쿼리를 JDBC 표준(`?`) 형식으로 변환한다.
    - e.g. `WHERE age >= :minAge` → `WHERE age >= ?`
    - 파라미터 이름으로 값을 설정하면 자동으로 순서에 맞게 바인딩한다.
- 동일한 Named Parameter가 여러 번 사용되면 각 위치에 동일한 값을 바인딩한다.
    - e.g. `WHERE age >= :min AND height >= :min` → 두 `?` 모두 같은 값
- 매핑 대상 클래스는 반드시 기본 생성자(파라미터 없는 생성자)를 가져야 한다.
    - 기본 생성자가 없으면 명확한 예외 메시지를 제공한다.
- private 필드에 접근하기 위해 `setAccessible(true)`를 사용한다.
    - Java 보안 정책에 따라 일부 환경에서는 동작하지 않을 수 있다.
- 데이터베이스 컬럼과 매칭되는 Java 필드가 없으면 해당 컬럼은 무시한다.
    - e.g. DB에 `extra_column`이 있지만 Java 클래스에 대응 필드가 없음 → 무시
- 타입이 일치하지 않으면 명확한 예외 메시지와 함께 실패한다.
    - e.g. DB의 VARCHAR를 Java int 필드에 매핑 시도 → 예외 발생
- Named Parameter는 콜론(`:`)으로 시작하는 식별자 형식만 지원한다.
    - e.g. `:paramName` (O), `${paramName}` (X), `@paramName` (X)
- 이번 단계에서는 컬렉션 타입(List, Set 등) 매핑은 지원하지 않는다.
    - 단순 기본 타입(primitive)과 래퍼 타입(wrapper), String, LocalDateTime만 지원한다.



---------


## 4단계 - ResultSet 자동 매핑 & Named Parameter

- 데이터베이스 조회 결과(ResultSet)를 Java 객체로 자동 변환하는 매핑 시스템을 구현하고, 쿼리에서 Named Parameter를 사용할 수 있도록 지원한다.
- ResultSet의 각 행을 Reflection을 사용해 Java 객체로 자동 변환한다.
    - e.g. ResultSet(id=1, name="John", age=30) → User 객체
    - 수동 매핑 코드 없이 자동으로 필드에 값을 설정한다.
- 데이터베이스 컬럼명(snake_case)을 Java 필드명(camelCase)으로 자동 변환한다.
    - e.g. DB 컬럼 `created_at` → Java 필드 `createdAt`
    - e.g. DB 컬럼 `user_name` → Java 필드 `userName`
- JDBC 타입을 Java 타입으로 자동 변환한다.
    - e.g. SQL INTEGER → Java int/Integer/long/Long
    - e.g. SQL VARCHAR → Java String
    - e.g. SQL TIMESTAMP → Java LocalDateTime
    - e.g. SQL BOOLEAN → Java boolean/Boolean
- Named Parameter(`:paramName`) 형식의 쿼리를 JDBC 표준(`?`) 형식으로 변환한다.
    - e.g. `WHERE age >= :minAge` → `WHERE age >= ?`
    - 파라미터 이름으로 값을 설정하면 자동으로 순서에 맞게 바인딩한다.
- 동일한 Named Parameter가 여러 번 사용되면 각 위치에 동일한 값을 바인딩한다.
    - e.g. `WHERE age >= :min AND height >= :min` → 두 `?` 모두 같은 값
- 매핑 대상 클래스는 반드시 기본 생성자(파라미터 없는 생성자)를 가져야 한다.
    - 기본 생성자가 없으면 명확한 예외 메시지를 제공한다.
- private 필드에 접근하기 위해 `setAccessible(true)`를 사용한다.
    - Java 보안 정책에 따라 일부 환경에서는 동작하지 않을 수 있다.
- 데이터베이스 컬럼과 매칭되는 Java 필드가 없으면 해당 컬럼은 무시한다.
    - e.g. DB에 `extra_column`이 있지만 Java 클래스에 대응 필드가 없음 → 무시
- 타입이 일치하지 않으면 명확한 예외 메시지와 함께 실패한다.
    - e.g. DB의 VARCHAR를 Java int 필드에 매핑 시도 → 예외 발생
- Named Parameter는 콜론(`:`)으로 시작하는 식별자 형식만 지원한다.
    - e.g. `:paramName` (O), `${paramName}` (X), `@paramName` (X)
- 이번 단계에서는 컬렉션 타입(List, Set 등) 매핑은 지원하지 않는다.
    - 단순 기본 타입(primitive)과 래퍼 타입(wrapper), String, LocalDateTime만 지원한다.


**TODO**

- [ ] ResultSetMapper 클래스 구현
- [ ] NamedParameterQuery 클래스 구현
- [ ] QueryBuilder, ResultSetMapper, NamedParameterQuery 통합된 하나의 단일 실행기를 구현. 실무에서 사용 가능한 Repository 패턴을 적용