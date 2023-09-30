import com.lesfurets.jenkins.unit.MethodCall
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import org.junit.Before
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

class JenkinsPipelineTest extends DeclarativePipelineTest {

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()
        helper.registerAllowedMethod('git', [Map], stringInterceptor)
        helper.registerAllowedMethod('codeNarc', [Map], stringInterceptor)
        helper.registerAllowedMethod('recordIssues', [Map], stringInterceptor)
        helper.registerAllowedMethod('junit', [Map], stringInterceptor)
        helper.registerAllowedMethod("parameters", [List])
        helper.registerAllowedMethod('buildAddUrl', [Map], stringInterceptor)
        helper.registerAllowedMethod('addDeployToDashboard', [Map], stringInterceptor)
    }

    @Test
    void checkExecutingWithoutErrors() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
    }

    @Test
    void checkCallStackContainsMvnCleanTest() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(mvn -f Java/pom.xml clean test)')
    }

    @Test
    void checkCallStackContainsMvnInstall() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(mvn -f Java/pom.xml install)')
    }

    @Test
    void checkCallStackContainsGradlew() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(./gradlew clean check)')
    }

    @Test
    void checkCallStackContainsMvnPomXml() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertThat(helper.callStack.stream()
                .filter { c -> c.methodName ==~ /sh/  }
                .map(MethodCall.&callArgsToString)
                .findAll { s -> s =~ /mvn.*Java\/pom.xml.*/ })
                .hasSize(2)
    }
}