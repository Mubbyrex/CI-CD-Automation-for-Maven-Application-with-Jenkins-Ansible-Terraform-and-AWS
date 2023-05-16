pipeline {
    agent any
ages {
        stage("Copy jenkinsfile") {
            steps{
                script {
                    echo 'Copying jenkinsfile'
                    sshagent(['ansible-server-key']){
                        sh 'scp -o StrictHostKeyChecking=no ansible/* root@139.144.187.97:/root'
                        // copy the ssh key from ec2 server to ansible server
                        withcredentials([sshUserPrivateKey(credentialsId: 'ec2-server-key', keyFileVariable: 'keyfile', usernameVariable: 'username')]) {
                            sh "scp -o StrictHostKeyChecking=no -i $keyfile root@139.144.187.97:/root/ssh-key.pem"
                        }
                    }
                }
            }
        }
 
    }


}