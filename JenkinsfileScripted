node('jenkins_agent') {
    stage("Setup Parameters") {
        script {
            properties([
                parameters([
                    string(
                        defaultValue: 'none',
                        description: 'version',
                        name: 'VERSION',
                        trim: true
                    )
                ])
            ])
        }
    }
    stage('Pipeline Quality Gates') {
        script {
            sh './gradlew clean check'
        }
    }
    stage('Checkout Project Repo') {
        dir('cparse') {
            git url: 'https://github.com/cparse/cparse.git'
        }
    }
    stage('Build') {
        sh 'make -C cparse'
    }
    stage('Execute Unit Tests') {
        sh 'make test -C cparse'
    }
    stage('Push into Artifactory') {
        script {
            server = Artifactory.server 'artifactory'
            def uploadSpec = """{
                "files": [{
                    "pattern": "cparse/core-shunting-yard.o",
                    "target": "cparse/${VERSION}/"
                }]
            }"""
            server.upload(uploadSpec)
        }
    }
    stage('Archive Artifacts') {
        script {
            def date = new Date()
            def data = "app version ${VERSION}\nhttp://localhost:8082/${VERSION}\n" + date
            writeFile(file: 'ARTIFACTORY.txt', text: data)
            sh "ls -l"
        }
        archiveArtifacts allowEmptyArchive: true,
        artifacts: 'ARTIFACTORY.txt, cparse/core-shunting-yard.o, **/test-results/test/TEST-*.xml',
        followSymlinks: false
    }
    post {
        always {
            recordIssues(tools: [codeNarc(pattern: '**/codenarc/test.xml', reportEncoding: 'UTF-8')])
        }
    }
}