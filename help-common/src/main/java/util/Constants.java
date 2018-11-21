package util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author chaoyangjiang
 * @time 2018.07.25 18:15:37
 * @description
 */
public class Constants {
    public static final String CHARACTER_COMMA = ",";
    public static final String CHARACTER_LEFT_BRACKET = "(";
    public static final String CHARACTER_RIGHT_BRACKET = ")";
    public static final String CHARACTER_PLUS = "+";
    public static final String CHARACTER_SUBTRACT = "-";
    public static final String CHARACTER_MULTIPLY = "*";
    public static final String CHARACTER_DIVIDE = "/";
    public static final String CHARACTER_MOD = "%";
    public static final String CHARACTER_AND = "&&";
    public static final String CHARACTER_OR = "||";
    public static final String CHARACTER_NOT = "!";
    public static final String CHARACTER_LESS = "<";
    public static final String CHARACTER_LESS_EQL = "<=";
    public static final String CHARACTER_GREAT = ">";
    public static final String CHARACTER_GREAT_EQL = ">=";
    public static final String CHARACTER_EQUALS = "==";
    public static final String CHARACTER_NOT_EQUALS = "!=";
    public static final String CHARACTER_BLANK = " ";
    public static final String CHARACTER_AT = "@";
    public static final String CHARACTER_SEMICOLON = ";";
    public static final String CHARACTER_COLON = ":";
    public static final String CHARACTER_UNDERLINE = "_";
    public static final String CHARACTER_FORWARD = "/";
    public static final String CHARACTER_POINT = ".";

    /**
     * rule.json
     */
    public static final String RULE_PRESET_VARIABLES = "preset_variables";
    public static final String RULE_LEVEL_EXPIRE_TIME = "level_expire_time";
    public static final String RULE_RULES = "rules";
    public static final String RULE_EXPRESSION = "expression";
    public static final String RULE_LEVEL = "level";
    public static final String RULE_PRIORITY = "priority";

    /**日期**/
    public static final String DATE_PATTERN_YYYYMMDDHH = "yyyyMMddHH";
    public static final DateTimeFormatter YYYYMMDDHH = DateTimeFormat.forPattern(Constants.DATE_PATTERN_YYYYMMDDHH);
}
