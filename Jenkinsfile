pipeline {
    agent any
    stages {
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
    }
}