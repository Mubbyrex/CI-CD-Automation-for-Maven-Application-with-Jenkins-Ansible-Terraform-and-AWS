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
        stage("build and push image") {
            steps {
                script {
                    echo "building docker image..."
                    withCrendentials([usernamePassword(crendentialsId: 'dockerhub',passwordVariable:'PASSWORD',usernameVariable:'USERNAME')]){
                        sh 'docker build -t mubbyrex/jenkins-demo:jma-2.0 .'
                        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
                        sh 'docker push mubbyrex/jenkins-demo:jma-2.0'
                    }  
                }
            }
        }
        stage('deploying the application...') {
            steps {
                script {
                    echo 'deploying the app....'
                }
            }
        }
    }
}