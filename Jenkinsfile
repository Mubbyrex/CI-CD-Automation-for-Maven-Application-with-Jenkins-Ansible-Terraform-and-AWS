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
        stage('deploying docker to AWS') {
            steps {
                script {
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
