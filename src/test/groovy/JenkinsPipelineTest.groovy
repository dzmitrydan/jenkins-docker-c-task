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
        //helper.registerAllowedMethod('junit', [Map], stringInterceptor)
        helper.registerAllowedMethod("parameters", [List])
        //helper.registerAllowedMethod('buildAddUrl', [Map], stringInterceptor)
        //helper.registerAllowedMethod('addDeployToDashboard', [Map], stringInterceptor)
    }

    @Test
    void checkExecutingWithoutErrors() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
    }

    @Test
    void checkCallStackContainsMakeTest() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(make test -C cparse)')
    }

    @Test
    void checkCallStackContainsMake() throws Exception {
        runScript('JenkinsfileTest')
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('sh(make -C cparse)')
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
                .findAll { s -> s =~ /make.*-C cparse/ })
                .hasSize(2)
    }
}