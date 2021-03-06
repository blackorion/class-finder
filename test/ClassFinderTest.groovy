import spock.lang.Specification
import utils.SystemOutReader

class ClassFinderTest extends Specification implements SystemOutReader {

    def 'should return empty list when classes file is empty'() {
        given:
        String filename = 'empty.classes.txt'
        String pattern = 'anyPattern'

        when:
        runClassFinder(filename, pattern)

        then:
        assertSearchResultContain('')
    }

    def 'should contain class name in result when matches pattern'() {
        given:
        String filename = 'classes.txt'
        String pattern = 'FooBarBaz'

        when:
        runClassFinder(filename, pattern)

        then:
        assertSearchResultContain 'a.b.FooBarBaz'
    }

    def 'should return classes ordered by class name ignoring package name'() {
        given:
        String filename = 'classes.txt'
        String pattern = 'FooBar'

        when:
        runClassFinder(filename, pattern)

        then:
        'c.d.FooBar\n' +
                'a.b.FooBarBaz\n' == outContent.toString()
    }

    def 'should return trimmed class names when result is shown'() {
        given:
        String filename = 'classes.txt'
        String pattern = 'YouveComeToThisPoint'

        when:
        runClassFinder(filename, pattern)

        then:
        'YouveComeToThisPoint\n' == outContent.toString()
    }

    def 'should return classes ordered by class name ignoring case'() {
        given:
        String filename = 'classes.txt'
        String pattern = 'Y'

        when:
        runClassFinder(filename, pattern)

        then:
        'YoureLeavingUsHere\n' +
                'YourEyesAreSpinningInTheirSockets\n' +
                'YouveComeToThisPoint\n' == outContent.toString()
    }

    def runClassFinder(String filename, String pattern) {
        ClassFinder.main([filename, pattern] as String[])
    }

    def assertSearchResultContain(String expected) {
        outContent.toString()
                .split('\n')
                .any({ it == expected })
    }

    def assertSearchResultEquals(String expected) {
        expected == ''
    }

}
