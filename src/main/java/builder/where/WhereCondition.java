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
        validateConditionNotNull();
        this.condition = new LogicalCondition(this.condition, condition, LogicalOperator.AND);
        return this;
    }

    public WhereCondition and(WhereCondition whereCondition) {
        validateConditionNotNull();
        this.condition = new LogicalCondition(this.condition, whereCondition.condition, LogicalOperator.AND);
        return this;
    }

    public WhereCondition or(ComparisonCondition condition) {
        validateConditionNotNull();
        this.condition = new LogicalCondition(this.condition, condition, LogicalOperator.OR);
        return this;
    }

    public WhereCondition or(WhereCondition whereCondition) {
        validateConditionNotNull();
        this.condition = new LogicalCondition(this.condition, whereCondition.condition, LogicalOperator.OR);
        return this;
    }

    private void validateConditionNotNull() {
        if (this.condition == null) {
            throw new IllegalStateException("WHERE 를 우선 지정해주세요.");
        }
    }

    public String toSql() {
        if (condition == null) {
            return "";
        }

        return "WHERE " + String.join(" ", condition.generateSqlString());
    }
}
