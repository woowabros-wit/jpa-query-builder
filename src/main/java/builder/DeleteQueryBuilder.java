package builder;

public class DeleteQueryBuilder {

    private String table;
    private WhereCondition whereCondition;

    public DeleteQueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public DeleteQueryBuilder where(WhereCondition condition) {
        whereCondition = condition;
        return this;
    }

    public String build() {
        if (table == null || table.isBlank()) {
            throw new IllegalStateException("table은 null일 수 없습니다.");
        }
        if (whereCondition == null) {
            throw new IllegalStateException("where 조건을 반드시 지정해주세요.");
        }
        return "DELETE FROM " + table
            + " WHERE " + whereCondition.generateWhereConditionString();
    }
}
