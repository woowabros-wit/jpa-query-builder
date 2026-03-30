package builder.where;

public class WhereCondition {

    private Condition condition;

    public static WhereCondition empty() {
        return new WhereCondition();
    }

    public WhereCondition where(ComparisonCondition condition) {
        this.condition = condition;
        return this;
    }

    public WhereCondition and(ComparisonCondition condition) {
        if (this.condition == null) {
            throw new IllegalStateException("AND 조건을 추가하기 전에 먼저 WHERE 조건을 지정해주세요.");
        }
        this.condition = new LogicalCondition(this.condition, condition, LogicalOperator.AND);
        return this;
    }

    public WhereCondition or(ComparisonCondition condition) {
        if (this.condition == null) {
            throw new IllegalStateException("OR 조건을 추가하기 전에 먼저 WHERE 조건을 지정해주세요.");
        }
        this.condition = new LogicalCondition(this.condition, condition, LogicalOperator.OR);
        return this;
    }

    public String toSql() {
        if (condition == null) {
            return "";
        }

        return "WHERE " + String.join(" ", condition.generateSqlString());
    }
}
