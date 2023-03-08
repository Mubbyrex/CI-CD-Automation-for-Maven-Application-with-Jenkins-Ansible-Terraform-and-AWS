pipeline {
    agent any
    tools {
        maven 'maven-3.9'
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