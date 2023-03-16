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
    def shellCmd="bash ./shell-cmd.sh"
    sshagent(['AWS-instance']) {
    sh "scp shell-cmd.sh ec2-user@34.205.174.181:/home/ec2-user"
    sh "scp docker-compose.yaml ec2-user@34.205.174.181:/home/ec2-user"
    sh "ssh -o StrictHostKeyChecking=no ec2-user@34.205.174.181 ${shellCmd}"
    }
}

def commitVersion() {
     withCredentials([usernamePassword(credentialsId: 'github-login-PAT', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'

        sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/Mubbyrex/Jenkins-Project.git"
        sh 'git add .'
        sh 'git commit -m "ci: version bump"'
        sh 'git push origin HEAD:main'
     }
}

return this
