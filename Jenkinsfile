pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage("build jar") {
            steps {
                script {
                    echo "building the application..."
                    sh 'mvn package'
                }
            }
        }
    }
        stage("deploying the application...") {
            steps{
                script {
                 echo 'deploying the app...'
                }
            }
        }
}