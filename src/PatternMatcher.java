public class PatternMatcher {

    private final String pattern;
    private final MatchingStrategy matchingStrategy;
    private int pointer;
    private String lastPatternWord = "";

    private PatternMatcher(String pattern, MatchingStrategy matchingStrategy) {
        this.pattern = pattern;
        this.matchingStrategy = matchingStrategy;
    }

    public static PatternMatcher of(String pattern) {
        MatchingStrategy strategy = hasUppercaseChar(pattern)
                ? MatchingStrategy.CASE_SENSITIVE
                : MatchingStrategy.CASE_INSENSITIVE;
        pattern = pattern.trim() + (pattern.endsWith(" ") ? ' ' : "");

        return new PatternMatcher(pattern, strategy);
    }

    private static boolean hasUppercaseChar(String pattern) {
        return pattern.codePoints()
                .mapToObj(c -> (char) c)
                .anyMatch(Character::isUpperCase);
    }

    public boolean matches(String name) {
        resetPointerPosition();
        String className = CommonUtils.fetchClassName(name);

        return fullPatternMatch(className) || spaceEndingCase(className);
    }

    private boolean fullPatternMatch(String str) {
        for (char sc : str.toCharArray()) {
            if (fitsPointerPatternChar(sc))
                if (isLastPatternChar())
                    return true;
                else
                    forwardPointer();
        }

        return false;
    }

    private boolean spaceEndingCase(String str) {
        return isPatternEndsUpWithSpace()
                && isLastPatternChar()
                && isLastWordMatchesPattern(str);
    }

    private boolean isLastWordMatchesPattern(String str) {
        int si = str.length() - 1;

        for (int i = lastPatternWord.trim().length() - 1; i >= 0; i--) {
            char sc = str.charAt(si);
            if (!areMatched(lastPatternWord.charAt(i), sc))
                return false;

            si--;
        }

        return true;
    }

    private boolean isPatternEndsUpWithSpace() {
        return pattern.endsWith(" ");
    }

    private void resetPointerPosition() {
        pointer = 0;
        cacheLastWordInPattern();
    }

    private void forwardPointer() {
        pointer++;
        cacheLastWordInPattern();
    }

    private void cacheLastWordInPattern() {
        char pc = pattern.charAt(pointer);

        if (pointer == 0 || Character.isUpperCase(pc))
            lastPatternWord = "";

        lastPatternWord += pc;
    }

    private boolean fitsPointerPatternChar(char c) {
        return areMatched(pattern.charAt(pointer), c);
    }

    private boolean areMatched(char patternChar, char comparedTo) {
        return patternChar == '*' || matchingStrategy.matches(patternChar, comparedTo);
    }

    private boolean isLastPatternChar() {
        return pointer == pattern.length() - 1;
    }

    enum MatchingStrategy {
        CASE_SENSITIVE {
            boolean matches(char first, char second) {
                return first == second;
            }
        },

        CASE_INSENSITIVE {
            boolean matches(char first, char second) {
                return first == Character.toLowerCase(second);
            }
        };

        abstract boolean matches(char first, char second);
    }

}