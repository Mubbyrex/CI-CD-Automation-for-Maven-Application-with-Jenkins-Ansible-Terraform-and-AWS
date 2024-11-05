#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        EC2_PUBLIC_IP = 'ec2-18-197-17-66.eu-central-1.compute.amazonaws.com'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
    

        stage('increment version') {
            steps {
                script {
                    gv.incrementVersion()
                }
            }
        }

        stage('build jar') {
            steps {
               script {
                  echo 'building application jar..'
                  gv.buildjar()
               }
            }
        }
        stage('build image') {
            steps {
                script {
                   env.IMAGE_NAME = "mubbyrex/maven-app:${env.IMAGE_VERSION}"
                   gv.buildImage()
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                  gv.deployAppOnEC2()
                }
            }
        }

        stage('commit version') {
            steps {
                script {
                    gv.commitVersion()
                }
            }
        }

    }
}
