pipeline {
    agent none
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
                sh 'autoconf --version'
                sh 'make --version'
                sh 'make -C cparse'
            }
        }
        stage('Execute Unit Tests') {
            steps {
                sh 'make test -C cparse'
            }
        }
        stage('Archive Artifacts') {
                archiveArtifacts allowEmptyArchive: true,
                artifacts: 'cparse/core-shunting-yard.o',
                followSymlinks: false
            }
        }
    }
}