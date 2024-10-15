#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        IMAGE_NAME = 'mubbyrex/maven-app:1.1'
        EC2_PUBLIC_IP = 'ec2-18-196-216-87.eu-central-1.compute.amazonaws.com'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
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

    }
}
