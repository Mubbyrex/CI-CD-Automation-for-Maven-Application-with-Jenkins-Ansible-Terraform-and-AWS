@library('jenkins-shared-library')
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
                    buildjar()
                }
            }
        }
        stage("build and push image") {
            steps {
                script {
                    buildimage()
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
