def buildjar() {
    echo "building the application..."
    sh 'mvn clean package'
}

def incrementVersion() {
    echo 'increasing the app version...'
    sh 'mvn build-helper:parse-version versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
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
    def ec2_instance = ${EC2_PUBLIC_IP}
    sshagent(['AWS-instance']) {
    sh "scp -o StrictHostKeyChecking=no shell-cmd.sh ${ec2_instance}:/home/ec2-user"
    sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2_instance}:/home/ec2-user"
    sh "ssh -o StrictHostKeyChecking=no ${ec2_instance} ${shellCmd}"
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

def provisionServer() {
    dir('terraform') {
    sh "terraform init"
    sh "terraform apply --auto-approve"
    EC2_PUBLIC_IP = sh(
        script: "terraform output ec2_public_ip",
        returnStdout: true
    ).trim()
}
}

return this
