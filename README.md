# jpa-query-builder

문자열 결합 없이 SQL SELECT 쿼리를 프로그래밍 방식으로 안전하게 생성하는 빌더 클래스를 구현한다.
메서드 체이닝을 통해 쿼리의 각 구성 요소를 순차적으로 조립하고, 최종적으로 실행 가능한 SQL 문자열을 반환한다.
조회할 컬럼, 테이블, 정렬 순서, 조회 개수를 지정할 수 있다.
e.g. SELECT id, name FROM users ORDER BY age DESC LIMIT 10
특정 컬럼을 지정하지 않으면 자동으로 전체 컬럼(*)을 선택한다.
메서드 호출 순서는 자유롭게 지정할 수 있다.
e.g. select().from().orderBy() 또는 from().orderBy().select() 모두 동일한 결과
필수 메서드(테이블 지정)를 호출하지 않으면 빌드 시 명확한 예외를 발생시킨다.
생성된 SQL은 JDBC의 PreparedStatement에서 직접 사용 가능한 형식이다.
테이블 지정(from)은 반드시 호출해야 하며, 호출하지 않으면 IllegalStateException을 발생시킨다.
조회 개수 제한(limit)은 양의 정수만 허용하며, 0 이하 값은 IllegalArgumentException을 발생시킨다.
동일한 메서드를 여러 번 호출하면 마지막 호출 값으로 덮어쓴다.
e.g. .select("id").select("name") → 최종 결과는 SELECT name
이번 단계에서는 단일 컬럼 정렬만 지원하며, WHERE 조건은 지원하지 않는다.
JOIN, GROUP BY, HAVING 등의 고급 기능은 이번 과제에서 제외한다.