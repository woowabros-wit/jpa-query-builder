package builder;

public class WhereCondition {

    private String column;
    private Operator operator;
    private String[] values;

    public WhereCondition(String column, Operator operator, String... values) {
        if (column == null || column.isBlank()) {
            throw new IllegalStateException("컬럼명은 null 또는 빈 문자열일 수 없습니다.");
        }
        operator.validate(values);

        this.column = column;
        this.operator = operator;
        this.values = values;
    }

    public String generateWhereConditionString() {
        return column + " " + operator.toSqlString(values);
    }
}
