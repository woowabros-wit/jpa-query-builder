package builder.where;

import java.util.ArrayList;
import java.util.List;

public class WhereCondition {

    private List<Condition> conditions;

    private WhereCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public static WhereCondition empty() {
        return new WhereCondition(new ArrayList<>());
    }

    public WhereCondition where(ComparisonCondition condition) {
        initConditions();
        conditions.add(condition);
        return this;
    }

    private void initConditions() {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
    }

    public WhereCondition and(ComparisonCondition condition) {
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalStateException("AND 조건을 추가하기 전에 먼저 WHERE 조건을 지정해주세요.");
        }
        int lastIndex = conditions.size() - 1;
        conditions.add(new LogicalCondition(conditions.get(lastIndex), condition, LogicalOperator.AND));
        conditions.remove(lastIndex);
        return this;
    }

    public WhereCondition or(ComparisonCondition condition) {
        if (conditions == null || conditions.isEmpty()) {
            throw new IllegalStateException("OR 조건을 추가하기 전에 먼저 WHERE 조건을 지정해주세요.");
        }
        int lastIndex = conditions.size() - 1;
        conditions.add(new LogicalCondition(conditions.get(lastIndex), condition, LogicalOperator.OR));
        conditions.remove(lastIndex);
        return this;
    }

    public String toSql() {
        if (conditions.isEmpty()) {
            return "";
        }

        List<String> whereConditionStrings = conditions.stream()
            .map(Condition::generateSqlString)
            .toList();
        return "WHERE " + String.join(" ", whereConditionStrings);
    }
}
