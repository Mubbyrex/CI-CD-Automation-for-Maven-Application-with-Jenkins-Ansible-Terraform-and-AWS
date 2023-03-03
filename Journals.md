Pull and run Jenkins container from dockerhub

```
docker run -p 8080:8080 -p 50000:50000 -d -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts
```

- install node and npm inside the shell of the jenkins container
- install maven as plug-in in jenkins
- create a new job
