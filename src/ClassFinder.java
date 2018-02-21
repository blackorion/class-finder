import java.io.IOException;

import static java.lang.System.out;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

class ClassFinder {

    public static void main(String[] args) throws IOException {
        new ClassFinder().runWith(AppProperties.of(args));
    }

    private void runWith(AppProperties properties) throws IOException {
        PatternMatcher comparator = PatternMatcher.of(properties.pattern);

        readAllLines(get(properties.filename)).stream()
                .filter(comparator::matches)
                .sorted(this::byClassName)
                .forEach(out::println);
    }

    private int byClassName(String first, String second) {
        return CommonUtils.fetchClassName(first)
                .compareTo(CommonUtils.fetchClassName(second));
    }

    static class AppProperties {

        final String filename;
        final String pattern;

        private AppProperties(String filename, String pattern) {
            this.filename = filename;
            this.pattern = pattern;
        }

        static AppProperties of(String[] args) {
            return new AppProperties(args[0], args[1]);
        }
    }

}