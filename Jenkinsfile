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

        stage("Excute ansible playbook"){
            steps{
                script {
                    echo "calling ansible playbook to configure ec2 instances"
                    sshagent(['ansible-server-key']){
                        sh '''
                            ssh -o StrictHostKeyChecking=no root@139.144.60.105
                            ls -l
                        '''
                        }

                    }
                    // def remote = [:]
                    // remote.name = "ansible-server"
                    // remote.host = "139.144.60.105"
                    // remote.allowAnyHosts = true

                    // withCredentials([sshUserPrivateKey(credentialsId: 'ansible-server-key', keyFileVariable: 'keyfile', usernameVariable: 'user')]){
                    //     remote.user = user
                    //     remote.identityFile = keyfile
                    //     sshCommand remote: remote, command: "ls -l"
                    // }

                }
            }
        // }
    }
}