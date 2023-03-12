def buildjar() {
    echo "building the application..."
    sh 'mvn clean package'
}

def buildImage() {
    echo "building docker image..."
    withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh "docker build -t mubbyrex/jenkins-demo:${IMAGE_NAME} ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push mubbyrex/jenkins-demo:${IMAGE_NAME}"
    }
}

def deployApp() {
    echo 'deploying the app....'
    echo 'testing automation.....'
}

return this
