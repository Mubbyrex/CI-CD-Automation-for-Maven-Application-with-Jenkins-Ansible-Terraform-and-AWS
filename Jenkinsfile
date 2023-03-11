def gv
pipeline {
    agent any
    tools {
        maven 'maven'
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
                    gv.buildjar()
                }
            }
        }
        stage("build and push image") {
            steps {
                script {
                    gv.buildImage()
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
