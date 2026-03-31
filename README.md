# jpa-query-builder

## 3단계 - CRUD 완성 & WHERE 조건

### 목표

- SQL SELECT 쿼리를 생성하는 빌더 클래스 구현

### 기능 요구 사항

- 데이터 삽입(INSERT), 수정(UPDATE), 삭제(DELETE)를 위한 쿼리 빌더를 구현하고, 1단계의 SELECT 빌더에 WHERE 조건 기능을 추가한다.
- INSERT 쿼리를 생성하여 새로운 데이터를 데이터베이스에 추가할 수 있다. 
  - e.g. INSERT INTO users (name, age, email) VALUES (?, ?, ?)

- INSERT 쿼리는 최소 1개 이상의 컬럼-값 쌍을 가져야 하며, 없으면 예외를 발생시킨다.
- INSERT/UPDATE 시 컬럼 순서가 추가한 순서대로 유지된다.

- UPDATE 쿼리를 생성하여 기존 데이터를 수정할 수 있다.
  - e.g. UPDATE users SET name = ?, age = ? WHERE id = ?
- SET 절에 여러 컬럼을 지정할 수 있다.

- DELETE 쿼리를 생성하여 데이터를 삭제할 수 있다.
  - e.g. DELETE FROM users WHERE age < ?
- WHERE 조건을 통해 삭제 대상을 특정할 수 있다.

- UPDATE와 DELETE 쿼리는 WHERE 조건을 필수로 요구한다.
- WHERE 없는 UPDATE/DELETE는 위험하므로 빌드 시 예외를 발생시킨다.
  - e.g. UPDATE users SET name = ? → 예외 발생 (WHERE 없음)


- 파라미터 값은 직접 쿼리 문자열에 포함하지 않고, 플레이스홀더(?)만 사용한다.
  - e.g. "name = ?" (O), "name = 'John'" (X)
- WHERE 조건은 문자열 형태로 전달하며, AND/OR 조합은 이번 단계에서 제외한다.
  - e.g. where("age >= ? AND status = ?") 형태로 한 번에 전달
- LinkedHashMap을 사용하여 컬럼 순서를 보장한다.

- 컬럼과 값을 하나씩 추가하거나, Map으로 한 번에 추가할 수 있다.

- WHERE 조건을 통해 수정 대상을 특정할 수 있다.

- SELECT 쿼리에 WHERE 조건을 추가하여 특정 조건의 데이터만 조회할 수 있다. 
  - e.g. SELECT * FROM users WHERE age >= ? AND status = ?
- 모든 쿼리에서 파라미터 플레이스홀더(?)를 사용하여 SQL 인젝션을 방지한다.
