package util;

import annotation.BlankSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testutil.TestArrayUtils;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @ParameterizedTest(name = "isBlank - 입력값이 공백문자이거나 null 인경우 true. input: [{0}]")
    @BlankSource
    void isBlank(String input) throws Exception {
        assertThat(StringUtils.isBlank(input)).isTrue();
    }

    @DisplayName("isBlank - 입력값이 공백문자가 아닌 경우 false")
    @Test
    void isBlank1() throws Exception {
        final String input = "a";
        assertThat(StringUtils.isBlank(input)).isFalse();
    }

    @ParameterizedTest(name = "isNotBlank - 입력값이 공백문자이거나 null 인경우 false. input: [{0}]")
    @BlankSource
    void isNotBlank(String input) throws Exception {
        assertThat(StringUtils.isNotBlank(input)).isFalse();
    }

    @DisplayName("isNotBlank - 입력값이 공백문자가 아닌 경우 true")
    @Test
    void isNotBlank1() throws Exception {
        final String input = "a";
        assertThat(StringUtils.isNotBlank(input)).isTrue();
    }

    @ParameterizedTest(name = "anyBlank - 입력값 중 하나라도 공백문자이거나 null 인경우 true. input: {0}")
    @MethodSource("anyBlank")
    void anyBlank(String[] input) throws Exception {
        assertThat(StringUtils.anyBlank(input)).isTrue();
    }

    private static Stream<Arguments> anyBlank() {
        return Stream.of(
                TestArrayUtils.toArguments(new String[]{null}),
                TestArrayUtils.toArguments(new String[]{""}),
                TestArrayUtils.toArguments(new String[]{" "}),
                TestArrayUtils.toArguments(new String[]{null, null}),
                TestArrayUtils.toArguments(new String[]{"", ""}),
                TestArrayUtils.toArguments(new String[]{" ", " "}),
                TestArrayUtils.toArguments(new String[]{null, ""}),
                TestArrayUtils.toArguments(new String[]{null, " "}),
                TestArrayUtils.toArguments(new String[]{"", null}),
                TestArrayUtils.toArguments(new String[]{" ", null}),
                TestArrayUtils.toArguments(new String[]{"a", null}),
                TestArrayUtils.toArguments(new String[]{"a", ""}),
                TestArrayUtils.toArguments(new String[]{"a", " "}),
                TestArrayUtils.toArguments(new String[]{null, "a"}),
                TestArrayUtils.toArguments(new String[]{"", "a"}),
                TestArrayUtils.toArguments(new String[]{" ", "a"})
        );
    }


    @ParameterizedTest(name = "anyBlank - 입력값이 모두 공백문자가 아닌 경우 false. input: {0}")
    @MethodSource("anyBlank1")
    void anyBlank1(String[] input) throws Exception {
        assertThat(StringUtils.anyBlank(input)).isFalse();
    }

    private static Stream<Arguments> anyBlank1() {
        return Stream.of(
                TestArrayUtils.toArguments(new String[]{"a"}),
                TestArrayUtils.toArguments(new String[]{"a", "b"})
        );
    }

    @ParameterizedTest(name = "allNotBlank - 입력값 중 하나라도 공백문자이거나 null 인경우 false. input: {0}")
    @MethodSource("allNotBlank")
    void allNotBlank(String[] input) throws Exception {
        assertThat(StringUtils.allNotBlank(input)).isFalse();
    }

    private static Stream<Arguments> allNotBlank() {
        return Stream.of(
                TestArrayUtils.toArguments(new String[]{null}),
                TestArrayUtils.toArguments(new String[]{""}),
                TestArrayUtils.toArguments(new String[]{" "}),
                TestArrayUtils.toArguments(new String[]{null, null}),
                TestArrayUtils.toArguments(new String[]{"", ""}),
                TestArrayUtils.toArguments(new String[]{" ", " "}),
                TestArrayUtils.toArguments(new String[]{null, ""}),
                TestArrayUtils.toArguments(new String[]{null, " "}),
                TestArrayUtils.toArguments(new String[]{"", null}),
                TestArrayUtils.toArguments(new String[]{" ", null}),
                TestArrayUtils.toArguments(new String[]{"a", null}),
                TestArrayUtils.toArguments(new String[]{"a", ""}),
                TestArrayUtils.toArguments(new String[]{"a", " "}),
                TestArrayUtils.toArguments(new String[]{null, "a"}),
                TestArrayUtils.toArguments(new String[]{"", "a"}),
                TestArrayUtils.toArguments(new String[]{" ", "a"})
        );
    }

    @DisplayName("allNotBlank - 입력값이 모두 공백문자가 아닌 경우 true")
    @Test
    void allNotBlank1() throws Exception {
        final String[] input = new String[] {"a"};
        assertThat(StringUtils.allNotBlank(input)).isTrue();
    }

}
