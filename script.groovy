def buildjar() {
    echo "building the application..."
    sh 'mvn package'
}

def buildImage() {
    echo "building docker image..."
    withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh 'docker build -t mubbyrex/jenkins-demo:jma-4.0 .'
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh 'docker push mubbyrex/jenkins-demo:jma-4.0'
    }
}

def deployApp() {
    echo 'deploying the app....'
    echo 'testing automation.....'
}

return this
