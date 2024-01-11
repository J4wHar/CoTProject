pipeline {
    agent any
    
    environment {
        WILDFLY_HOME = '/opt/wildfly'
        M3_HOME = '/opt/maven'
        PROJECT_DIR = 'server'
        CLIENT_DIR = 'client'  // Assuming this is the path to your PWA files
    }
    
    stages {
        stage('Build') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        sh "$M3_HOME/bin/mvn clean install"
                    }
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        sh "$M3_HOME/bin/mvn test"
                    }
                }
            }
        }
        
        stage('Deploy PWA') {
            steps {
                script {
                    // Replace 'your_pwa_files' with the actual path inside '/client'
                    sh "cp -r ${CLIENT_DIR}/ ${WILDFLY_HOME}/standalone/deployments/"
                }
            }
        }

        stage('Deploying Backend to WildFly') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        sh "$WILDFLY_HOME/bin/jboss-cli.sh --connect -u=\"admin\" -p=\"admin\"  --command=\"deploy --force target/waspsecurity-1.0-SNAPSHOT.war\""
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                
                sh "rm -rf ${PROJECT_DIR}/target"
            }
        }
    }
}
