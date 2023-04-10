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

def deployAppOnEC2() {
    def shellCmd="bash ./shell-cmd.sh ${IMAGE_NAME}"
    sshagent(['AWS-instance']) {
    sh "scp shell-cmd.sh ec2-user@34.205.174.181:/home/ec2-user"
    sh "scp docker-compose.yaml ec2-user@34.205.174.181:/home/ec2-user"
    sh "ssh -o StrictHostKeyChecking=no ec2-user@34.205.174.181 ${shellCmd}"
    }
}



//function to deploy to k8s on linode cluster via jenkins
def deploytoK8s() {
     echo 'deploying docker image...'
    withKubeConfig([credentialsId: 'lke-crendentials', serverUrl: 'https://160e3ecc-c960-463a-a788-a6f63d28eb91.us-southeast-1.linodelke.net:443']) {
        sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
        sh 'envsubst < kubernetes/services.yaml | kubectl apply -f -'
    }
}

def commitVersion() {
     withCredentials([usernamePassword(credentialsId: 'github-pat', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh 'git config --global user.email "jenkins@example.com"'
        sh 'git config --global user.name "jenkins"'

        sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/Mubbyrex/Jenkins-Project.git"
        sh 'git add .'
        sh 'git commit -m "ci: version bump"'
        sh 'git push origin HEAD:main'
     }
}

return this
