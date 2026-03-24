# jpa-query-builder

## 2단계 - 기본 SELECT 쿼리 빌더

### 목표

- SQL SELECT 쿼리를 생성하는 빌더 클래스 구현

### 기능 요구 사항

- 메서드 체이닝 이용
- 순서는 자유롭게 지정 가능 (ex: `select().from().orderBy()`, `from().select().orderBy()`는 동일한 결과)
- where, join, group by, having 등의 기능은 지원하지 않음

### 요청 및 응답 명세

- 요청
    - (optional) 조회할 컬럼 목록 (없으면 자동으로 전체 컬럼 `*` 선택)
    - (required) 테이블
    - (optional) 정렬 순서
    - (optional) 조회 개수
- 요청에 테이블이 포함되지 않으면 IllegalStateException 발생
- 조회 개수는 양의 정수만 허용하며, 0 또는 음수인 경우 IllegalArgumentException 발생
- 응답
    - 실행 가능한 SQL 문자열
    - PreparedSatement에서 직접 사용 가능

### Memo

Fluent API 패턴이란?

- 메서드 체이닝을 통해 객체 설정이나 동작을 자연스럽게 이어서 표현할 수 있도록 설계하느 API 스타일
- 빌더 패턴과는 무엇이 다른가?
    - 빌더 패턴: 복잡한 객체 생성을 단계적으로 구성하기 위한 **생성 패턴**