package jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SelectQueryBuilder {

    List<String> selectColumns = new ArrayList<>();
    String fromTable = null;
    String orderByColumn = null;
    String orderDirection = null;
    long limitValue = Long.MAX_VALUE;

    /**
     * SELECT 절 지정
     * @param columns 컬럼명 (가변 인자)
     * @return this (메서드 체이닝)
     */
    public SelectQueryBuilder select(String... columns) {
        List<String> inputColumns = Arrays.stream(columns).toList();
        int uniqueColumnCnt = Set.of(columns).size();

        if (inputColumns.isEmpty()) {
            selectColumns.add("*");
        } else if (uniqueColumnCnt != inputColumns.size()) {
            throw new IllegalArgumentException("select에 중복된 칼럼이 있음");
        } else {
            this.selectColumns.addAll(inputColumns);
        }
        return this;
    }

    /**
     * FROM 절 지정
     * @param table 테이블명
     * @return this
     */
    public SelectQueryBuilder from(String table) {
        if (table == null) {
            throw new IllegalStateException("from에 테이블은 필수값임");
        }
        this.fromTable = table;
        return this;
    }

    /**
     * ORDER BY 절 지정
     * @param column 정렬 컬럼
     * @param direction "ASC" 또는 "DESC"
     * @return this
     */
    public SelectQueryBuilder orderBy(String column, String direction) {
        this.orderByColumn = column;
        this.orderDirection = direction;
        if (this.orderByColumn != null && this.orderDirection == null) {
            this.orderDirection = "ASC";
        } else if (this.orderByColumn == null && this.orderDirection != null) {
            throw new IllegalArgumentException("order by의 칼럼이 없이 정렬방향을 설정할 수 없음");
        }
        return this;
    }

    /**
     * LIMIT 절 지정
     * @param limit 조회 개수
     * @return this
     */
    public SelectQueryBuilder limit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit는 양의 정수여야 함");
        }
        this.limitValue = limit;
        return this;
    }

    /**
     * SQL 문자열 생성
     * @return 생성된 SQL
     */
    public String build() {
        if (!isValid()) {
            throw new IllegalStateException("실행할 수 없는 sql임");
        }
        String selectQueryString = "SELECT " + getSelectColumnNames() + " ";
        String fromQueryString = "FROM " + this.fromTable + " ";
        String orderByQueryString =  (this.orderByColumn != null && this.orderDirection != null) ? "ORDER BY " + this.orderByColumn + " " + this.orderDirection + " " : "";
        String limitQueryString = (this.limitValue != Long.MAX_VALUE) ? "LIMIT " + this.limitValue : "";
        return (selectQueryString + fromQueryString + orderByQueryString + limitQueryString).trim();
    }

    private boolean isValid() {
        return fromTable != null;
    }

    private String getSelectColumnNames() {
        return (this.selectColumns.isEmpty()) ? "*" : String.join(", ", this.selectColumns);
    }
}
