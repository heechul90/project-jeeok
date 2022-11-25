package com.jeeok.jeeokmember.config.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.util.Locale;
import java.util.Stack;

import static org.springframework.util.StringUtils.hasText;

public class CustomP6SpySqlFormat implements MessageFormattingStrategy {

    public static final String ALLOW_FILTER = "com.jeeok.shop";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);

        //sql이 없으면 출력하지 않음
        if (sql.trim().isEmpty()) {
            return "";
        }

        //stack 을 구성하는 Format 을 만든다
        return sql + createStack(connectionId, elapsed);
    }

    //stack 콘솔 표기
    private String createStack(int connectionId, long elapsed) {
        Stack<String> callStack = new Stack<>();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        for (StackTraceElement stackTraceElement : stackTrace) {
            String trace = stackTraceElement.toString();

            //trace 항목을 보고 내게 맞는 것만 필터
            if (trace.startsWith(ALLOW_FILTER)) {
                callStack.push(trace);
            }
        }

        StringBuffer sb = new StringBuffer();
        int order = 1;

        while (callStack.size() != 0) {
            sb.append("\n\t\t" + (order++) + "." + callStack.pop());
        }

        return new StringBuffer().append("\n\n\tConnection ID :").append(connectionId)
                .append(" | Execution Time : ").append(elapsed).append(" ms\n")
                .append("\n\tExecution Time : ").append(elapsed).append(" ms\n")
                .append("\n\tCall Stack : ").append(sb).append("\n")
                .append("\n=======================================")
                .toString();
    }

    private String formatSql(String category, String sql) {
        if(!hasText(sql) || !hasText(sql.trim())) return sql;

        //Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String temsql = sql.trim().toLowerCase(Locale.ROOT);
            if (temsql.startsWith("create") || temsql.startsWith("alter") || temsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }
        return sql;
    }
}
