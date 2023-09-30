pipeline {
    agent {label 'jenkins_agent'}
    stages {
        stage("Setup Parameters") {
            steps {
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
        }
        stage('Checkout Project Repo') {
            steps {
                dir('cparse') {
                    git url: 'https://github.com/cparse/cparse.git'
                }
            }
        }
        stage('Build') {
            steps {
                sh 'make -C cparse'
            }
        }
        stage('Execute Unit Tests') {
            steps {
                sh 'make test -C cparse'
            }
        }
        stage('Push into Artifactory') {
            steps {
                script {
                    server = Artifactory.server 'artifactory'
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "cparse/core-shunting-yard.o",
                                "target": "cparse/${VERSION}/"
                            }
                        ]}"""
                    server.upload(uploadSpec)
                }
            }
        }
        stage('Archive Artifacts') {
            steps {
                script {
                   def date = new Date()
                   def data = "app version ${VERSION}\nhttp://localhost:8082/${VERSION}\n" + date
                   writeFile(file: 'ARTIFACTORY.txt', text: data)
                   sh "ls -l"
                }
                archiveArtifacts allowEmptyArchive: true,
                artifacts: 'ARTIFACTORY.txt, cparse/core-shunting-yard.o',
                followSymlinks: false
            }
        }
    }
    post {
        always {
            recordIssues(tools: [codeNarc(pattern: '**/codenarc/test.xml', reportEncoding: 'UTF-8')])
        }
    }
}