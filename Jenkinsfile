#!/usr/bin/env groovy

def gv

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        IMAGE_NAME = 'mubbyrex/maven-app:1.1'
        EC2_PUBLIC_IP = 'ec2-18-156-135-116.eu-central-1.compute.amazonaws.com'
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
            environment {
               AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
               AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
            }
            steps {
                script {
                  gv.deployAppOnK8s()
                }
            }
        }

    }
}
