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

        stage('increment version') {
            steps {
                script {
                    echo 'increasing the app version...'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
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

        stage('provision server') {
            environment {
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
                TF_VAR_env_prefix = 'test'
            }
            steps {
                script {
                    dir('terraform') {
                        sh "terraform init"
                        sh "terraform apply --auto-approve"
                        EC2_PUBLIC_IP = sh(
                            script: "terraform output ec2_public_ip",
                            returnStdout: true
                        ).trim()
                    }
                }
            }
        }

// give your instance time to boot up before deployment
        stage('deploying docker to Kubernetes') {
            steps {
                script {
                    sleep(secs: 90, unit: 'SECONDS')
                    gv.deploytoK8s()
                }
            }
        }
        stage('committing version to github') {
            steps {
                script {
                    gv.commitVersion()
                }
            }
        }
    }
}
