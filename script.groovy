def buildjar() {
    echo "building the application..."
    // sh 'mvn clean package'
}

def buildImage() {
    echo "building docker image..."
    withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh "docker build -t ${IMAGE_NAME} ."
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push ${IMAGE_NAME}"
    }
}

def deployAppOnEC2() {
    def shellCmd="bash ./shell-cmd.sh ${IMAGE_NAME}"
    def ec2_instance = ${EC2_PUBLIC_IP}
    sshagent(['ec2-instance']) {
    sh "scp -o StrictHostKeyChecking=no shell-cmd.sh ${ec2_instance}:/home/ec2-user"
    sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2_instance}:/home/ec2-user"
    sh "ssh -o StrictHostKeyChecking=no ${ec2_instance} ${shellCmd}"
    }
}

return this
