package builder;

import builder.where.ComparisonCondition;
import builder.where.WhereCondition;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateQueryBuilder {

    private String table;
    private Map<String, String> columnValues;
    private WhereCondition whereCondition;

    public UpdateQueryBuilder table(String table) {
        this.table = table;
        return this;
    }

    public UpdateQueryBuilder set(String column, String value) {
        if (columnValues == null) {
            columnValues = new LinkedHashMap<>();
        }
        columnValues.put(column, value);
        return this;
    }

    public UpdateQueryBuilder where(ComparisonCondition condition) {
        whereCondition = WhereCondition.empty();
        whereCondition.where(condition);
        return this;
    }

    public UpdateQueryBuilder and(ComparisonCondition condition) {
        whereCondition.and(condition);
        return this;
    }

    public UpdateQueryBuilder or(ComparisonCondition condition) {
        whereCondition.or(condition);
        return this;
    }

    public String build() {
        if (table == null || table.isBlank()) {
            throw new IllegalStateException("table은 null일 수 없습니다.");
        }
        if (whereCondition == null) {
            throw new IllegalStateException("where 조건을 반드시 지정해주세요.");
        }
        return "UPDATE " + table
            + " SET " + generateSetString()
            + " " + whereCondition.toSql();
    }

    private String generateSetString() {
        List<String> columnValueStrings = columnValues.entrySet().stream()
            .map(entry -> entry.getKey() + " = " + entry.getValue())
            .toList();
        return String.join(", ", columnValueStrings);
    }
}
