package builder;

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

    public UpdateQueryBuilder where(WhereCondition condition) {
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
        return "UPDATE " + table
            + " SET " + generateSetString()
            + " WHERE " + whereCondition.generateWhereConditionString();
    }

    private String generateSetString() {
        List<String> columnValueStrings = columnValues.entrySet().stream()
            .map(entry -> entry.getKey() + " = " + entry.getValue())
            .toList();
        return String.join(", ", columnValueStrings);
    }
}
