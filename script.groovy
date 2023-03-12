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

def commitVersion() {
     withCredentials([usernamePassword(credentialsId: 'Github-login', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh 'github config --global user.email "jenkins@example.com"'
        sh 'github config --global user.name "jenkins"'

        sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/Mubbyrex/Jenkins-Project.git"
        sh 'git add .'
        sh 'git commit -m "ci: version bump"'
        sh 'git push origin HEAD:main'
     }
}

return this
