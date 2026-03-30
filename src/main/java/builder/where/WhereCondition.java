package builder.where;

public class WhereCondition {

    private String column;
    private ComparisonOperator comparisonOperator;
    private String[] values;

    public WhereCondition(String column, ComparisonOperator comparisonOperator, String... values) {
        if (column == null || column.isBlank()) {
            throw new IllegalStateException("컬럼명은 null 또는 빈 문자열일 수 없습니다.");
        }
        comparisonOperator.validate(values);

        this.column = column;
        this.comparisonOperator = comparisonOperator;
        this.values = values;
    }

    public String generateWhereConditionString() {
        return comparisonOperator.toSqlString(column, values);
    }
}
