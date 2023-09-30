# Jenkins Task

## Description
### Composition of the project
1. Docker with Docker Compose
2. Jenkins
   - Jenkins Master
   - Jenkins Agent
   - Jenkins Pipeline
3. Project [jenkins-task](https://github.com/dzmitrydan/jenkins-task) (working project)
   - Build tool: [Gradle](https://gradle.org)
   - JenkinsPipelineUnit tests: [Groovy](https://groovy-lang.org) and [JUnit](https://junit.org/junit4)
   - Static code analyzer: [CodeNarc](https://codenarc.org)
   - Jenkinsfile
   - Docker-compose files
4. Project for pipeline [aircompany](https://github.com/dzmitrydan/aircompany)
5. Artifactory (cloud, trial)

#### Precondition
- Docker with components is installed

### 1. Install and Run Jenkins With Docker Compose
#### 1.1 Install Jenkins by Docker Compose
Run Docker Compose:
```
docker-compose -f jenkins-docker-compose.yml up
```
Stop Docker Compose:
```
docker-compose -f jenkins-docker-compose.yml down
```
Open bash shell of docker container:
```
docker exec -it jenkins bash
```
#### 1.2 Login and install suggested Jenkins plugins
#### 1.3 Install additional Jenkins plugins
- Artifactory Plugin
- Warnings Next Generation Plugin

#### 1.4 Install Jenkins with Agent by Docker Compose
Run Docker Compose:
```
docker-compose -f jenkins-agent-docker-compose.yml up
```
Stop Docker Compose:
```
docker-compose -f jenkins-agent-docker-compose.yml down
```

#### 1.5 Generate SSH keys (public, private)
```
ssh-keygen -t rsa -f jenkins_agent
```

#### 1.6 In the Jenkins settings (Global credentials) add private SSH keys
- SSH `Username with private key`
- ID: `jenkins_agent`
- Username: `jenkins`
- Enter private SSH key

#### 1.7 Node settings:
- Remote Root Directory: `/home/jenkins/agent`
- Host: `agent`
- Launch method: `Launch agents via SSH`
- Credentials: select created
- Host Key Verification Strategy: `Non verifying Verification Strategy`
- Advanced
  - Java Path: `/opt/java/openjdk/bin/java`; 
  - Connection Timeout Seconds: `60`
  - Max Number of Retries: `10`
  - Seconds To Wait Between Retries: `15`
  - `Use TCP_NODELAY flag on the SSH connection`: check
  
![Nodes screenshot](readme-assets/jenkins-nodes.png)

### 2. Jenkins Pipeline Settings
- The project is parameterized. The parameter `VERSION` is passed for the directory in the Artifactory.
- Definition: `Pipeline script`
- SCM: `Git`
- Repository URL: project `jenkins-task` url
- Branch Specified: `*/main`

The `VERSION` parameter with the artifact uploaded to the Artifactory is put in the `ARTIFACTORY.txt` in the Archive Artifacts.

ARTIFACTORY.txt:
```
app version 2.19
https://blesstask.jfrog.io/ui/repos/tree/General/aircompany/none
Wed Sep 27 21:24:33 UTC 2023
```

Jenkins dashboard
- This chart **Test Result Trend** shows the result of passing Unit tests
- This chart **CodeNarc Warnings Trend** shows the result CodeNarc check

![Pipeline screenshot](readme-assets/jenkins-pipeline-01.png)

### 3. Artifactory
The Artifactory of the cloud version was used on the project (14-Day Trial).

Jenkins settings for Artifactory
- System > JFrog
  - JFrog Platform Instances
    - Instance ID: `artifactory`
    - JFrog Platform URL: `https://blesstask.jfrog.io`
    - Username: username for Artifactory
    - Password: password for Artifactory

![Artifactory screenshot](readme-assets/artifactory.png)

### 4. Jenkins Pipeline Run
Trigger a Jenkins build on Git commit
- Configuring Jenkins (Manage Jenkins > Configure > System Advanced > Check 'Specify another hook url' > Copy this URL)
- Configuring GitHub Repository (add Webhook in the repository Settings)
- Configuring Jenkins Pipeline (Project configuration > Build Triggers > Github hook trigger for GITScm Polling)

![Jenkins_agent_logs screenshot](readme-assets/jenkins-agent-logs.png)
![Pipeline screenshot](readme-assets/jenkins-pipeline-02.png)

### 5. Quality Gates (for project `jenkins-task`)
- [CodeNarc](https://codenarc.org)
- Warnings Next Generation Plugin
- Tests for Groovy pipeline: [JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit)

![Jenkins-codenarc screenshot](readme-assets/jenkins-codenarc.png)

Run pipeline tests:
```
./gradlew test
```
Run CodeNarc check:
```
./gradlew check
```
