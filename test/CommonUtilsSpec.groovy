import spock.lang.Specification

class CommonUtilsSpec extends Specification {

    def 'should return name unchanged when given string does not contain package name'() {
        expect:
        CommonUtils.fetchClassName('ClassName') == 'ClassName'
    }

    def 'should return class name only when given string contains package name'() {
        expect:
        CommonUtils.fetchClassName('package.ClassName') == 'ClassName'
    }

}