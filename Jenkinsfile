def gv

pipeline {
    agent any
    tools {
        maven 'maven-3.9'
    } 
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    echo "building the application..."
                    sh 'mvn package'
                }
            }
        }
        stage("build and push image") {
            steps {
                script {
                    buildImage()
                }
            }
        }
        stage('deploying the application...') {
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }
}
