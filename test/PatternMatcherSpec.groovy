import spock.lang.Specification
import spock.lang.Unroll

class PatternMatcherSpec extends Specification {

    @Unroll
    def 'should return true when matched pattern is in right order'() {
        given:
        def matcher = createComparatorFor(pattern)

        expect:
        matcher.matches(comparableString)


        where:
        pattern | comparableString
        'FB'    | 'FB'
        'FB'    | 'a.b.FooBarBaz'
        'FB'    | 'a.b.FooBarBaz'
        'FB'    | 'c.d.FooBar'
        'FoBa'  | 'a.b.FooBarBaz'
        'FoBa'  | 'c.d.FooBar'
        'FBar'  | 'a.b.FooBarBaz'
        'FBar'  | 'c.d.FooBar'
    }

    def 'should return false when pattern chars are in wrong order'() {
        given:
        def matcher = createComparatorFor('BF')

        expect:
        isNot matcher.matches('c.d.FooBar')
    }

    def 'should ignore package name when matches pattern'() {
        given:
        def matcher = createComparatorFor('cBar')

        expect:
        isNot matcher.matches('c.o.FooBar')
    }

    def 'should match case sensitive when pattern contains uppercase characters'() {
        given:
        def matcher = createComparatorFor('fBb')

        expect:
        isNot matcher.matches('FooBarBaz')
    }

    def 'should match case insensitive when lowercase pattern given'() {
        given:
        def matcher = createComparatorFor('fbb')

        expect:
        matcher.matches('FooBarBaz')
    }

    @Unroll("pattern: '#pattern', comparableString: '#comparableString'")
    def 'should match the string ending with same word as in pattern when pattern ends with space char'() {
        given:
        def matcher = createComparatorFor(pattern)

        expect:
        matcher.matches(comparableString)

        where:
        pattern  | comparableString
        'FBar '  | 'FooBar'
        'FBar '  | ' FooBar '
        'FBar  ' | ' FooBar '
    }

    @Unroll
    def 'should not match the string ending with different word then pattern when pattern ends with space char'() {
        given:
        def matcher = createComparatorFor(pattern)

        expect:
        isNot matcher.matches(comparableString)

        where:
        pattern | comparableString
        'FBar ' | 'FooBarBaz'
        'fbar ' | 'FooBar'
    }

    @Unroll
    def 'should accept wildcard as a replacement for a pattern missing letter'() {
        given:
        def matcher = createComparatorFor(pattern)

        expect:
        matcher.matches(comparableString)

        where:
        pattern  | comparableString
        'B*rBaz' | 'FooBarBaz'
        'B*rB*z' | 'FooBarBaz'
        '*'      | 'FooBarBaz'
        '******' | 'FooBarBaz'
    }

    @Unroll
    def 'should process patterns ending with space and containing wildcard'() {
        given:
        def matcher = createComparatorFor(pattern)

        expect:
        matcher.matches(comparableString)

        where:
        pattern   | comparableString
        'B*rBaz ' | 'FooBarBaz'
        'B*rB*z ' | 'FooBarBaz'
        '* '      | 'FooBarBaz'
        '****** ' | 'FooBarBaz'
    }

    PatternMatcher createComparatorFor(String s) {
        PatternMatcher.of(s)
    }

    def isNot(def result) {
        !result
    }
}