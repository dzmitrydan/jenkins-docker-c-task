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
                git url: 'https://github.com/cparse/cparse.git'
            }
        }
        stage('Build') {
            steps {
                sh 'make'
            }
        }
        stage('Execute Unit Tests') {
            steps {
                sh 'autoconf --version'
                sh 'make test'
            }
        }
        stage('Push into Artifactory') {
            steps {
                script {
                    server = Artifactory.server 'artifactory'
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "core-shunting-yard.o",
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
                artifacts: 'ARTIFACTORY.txt, core-shunting-yard.o',
                followSymlinks: false
            }
        }
    }
}