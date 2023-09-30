pipeline:
  agent:
    label: 'jenkins_agent'
  stages:
    - stage:
        name: 'Setup Parameters'
        steps:
          - script:
              script: |
                properties([
                  parameters([
                    string(
                      defaultValue: 'none',
                      description: 'version',
                      name: 'VERSION',
                      trim: true
                    )
                  ])
                ])
          - stage:
              name: 'Pipeline Quality Gates'
              steps:
                - script:
                    script: |
                      sh './gradlew clean check'
          - stage:
              name: 'Checkout Project Repo'
              steps:
                - dir:
                    dir: 'cparse'
                    git:
                      url: 'https://github.com/cparse/cparse.git'
          - stage:
              name: 'Build'
              steps:
                - sh: 'make -C cparse'
          - stage:
              name: 'Execute Unit Tests'
              steps:
                - sh: 'make test -C cparse'
          - stage:
              name: 'Push into Artifactory'
              steps:
                - script:
                    script: |
                      server = Artifactory.server 'artifactory'
                      def uploadSpec = """{
                        "files": [
                          {
                            "pattern": "cparse/core-shunting-yard.o",
                            "target": "cparse/${VERSION}/"
                          }
                        ]
                      }"""
                      server.upload(uploadSpec)
          - stage:
              name: 'Archive Artifacts'
              steps:
                - script:
                    script: |
                      def date = new Date()
                      def data = "app version ${VERSION}\nhttp://localhost:8082/${VERSION}\n" + date
                      writeFile(file: 'ARTIFACTORY.txt', text: data)
                      sh "ls -l"
                - archiveArtifacts:
                    allowEmptyArchive: true
                    artifacts: 'ARTIFACTORY.txt, cparse/core-shunting-yard.o, **/test-results/test/TEST-*.xml'
                    followSymlinks: false
  post:
    always:
      recordIssues:
        tools:
          - codeNarc:
              pattern: '**/codenarc/test.xml'
              reportEncoding: 'UTF-8'