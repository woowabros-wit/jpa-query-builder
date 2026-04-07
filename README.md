# jpa-query-builder

데이터베이스 조회 결과(ResultSet)를 Java 객체로 자동 변환하는 매핑 시스템을 구현하고, 쿼리에서 Named Parameter를 사용할 수 있도록 지원한다.

ResultSet의 각 행을 Reflection을 사용해 Java 객체로 자동 변환한다.

e.g. ResultSet(id=1, name="John", age=30) → User 객체
수동 매핑 코드 없이 자동으로 필드에 값을 설정한다.
데이터베이스 컬럼명(snake_case)을 Java 필드명(camelCase)으로 자동 변환한다.

e.g. DB 컬럼 created_at → Java 필드 createdAt
e.g. DB 컬럼 user_name → Java 필드 userName
JDBC 타입을 Java 타입으로 자동 변환한다.

e.g. SQL INTEGER → Java int/Integer/long/Long
e.g. SQL VARCHAR → Java String
e.g. SQL TIMESTAMP → Java LocalDateTime
e.g. SQL BOOLEAN → Java boolean/Boolean
Named Parameter(:paramName) 형식의 쿼리를 JDBC 표준(?) 형식으로 변환한다.

e.g. WHERE age >= :minAge → WHERE age >= ?
파라미터 이름으로 값을 설정하면 자동으로 순서에 맞게 바인딩한다.
동일한 Named Parameter가 여러 번 사용되면 각 위치에 동일한 값을 바인딩한다.

e.g. WHERE age >= :min AND height >= :min → 두 ? 모두 같은 값
매핑 대상 클래스는 반드시 기본 생성자(파라미터 없는 생성자)를 가져야 한다.

기본 생성자가 없으면 명확한 예외 메시지를 제공한다.
private 필드에 접근하기 위해 setAccessible(true)를 사용한다.

Java 보안 정책에 따라 일부 환경에서는 동작하지 않을 수 있다.
데이터베이스 컬럼과 매칭되는 Java 필드가 없으면 해당 컬럼은 무시한다.

e.g. DB에 extra_column이 있지만 Java 클래스에 대응 필드가 없음 → 무시
타입이 일치하지 않으면 명확한 예외 메시지와 함께 실패한다.

e.g. DB의 VARCHAR를 Java int 필드에 매핑 시도 → 예외 발생
Named Parameter는 콜론(:)으로 시작하는 식별자 형식만 지원한다.

e.g. :paramName (O), ${paramName} (X), @paramName (X)
이번 단계에서는 컬렉션 타입(List, Set 등) 매핑은 지원하지 않는다.

단순 기본 타입(primitive)과 래퍼 타입(wrapper), String, LocalDateTime만 지원한다.