pipeline {
    agent any
    stages {
        stage("Copy jenkinsfile") {
            steps{
                script {
                    echo 'Copying jenkinsfile'
                    sshagent(['ansible-server-key']){
                        sh 'scp -o StrictHostKeyChecking=no ansible/* root@139.144.60.105:/root'
                        
                        // copy the ssh key from ec2 server to ansible server
                        withCredentials([sshUserPrivateKey(credentialsId: 'ec2-server-key', keyFileVariable: 'keyfile', usernameVariable: 'user')]) {
                            sh 'scp $keyfile root@139.144.60.105:/root/ssh-key.pem'
                        }

                    }
                }
            }
        }
 
    }


}