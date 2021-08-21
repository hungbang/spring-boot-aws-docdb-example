#!/usr/bin/env groovy
pipeline {

    agent any
    environment {
        VERSION = sh(returnStdout: true, script: 'mvn exec:exec -Dexec.executable=awk -Dexec.args="\'BEGIN {printf(\\"%s-\\", gensub(/-SNAPSHOT/,\\"\\", \\"g\\", \\"\\${project.version}\\"));system(\\"git rev-parse --short=5 HEAD\\")}\'" --non-recursive -q').trim()
    }
    tools {
        maven 'maven3.8.1'
        jdk 'JDK1.8'
    }
    stages {
        stage ('Maven Build ') {
            when { anyOf { branch 'next'; branch 'develop' } }
            steps {
                script {
                    sh '''
                        mvn clean install -DskipTests
                    '''
                }
            }
        }

        stage('Generate keystore') {
            steps {
                script {
                    sh '''
                        ./generateKeyStore-linux.sh
                    '''
                }
            }
        }

        stage ('Push Docker Image to Amazon ECR') {
            when { anyOf { branch 'next'; branch 'develop'} }
            steps {
                script {
                    echo "Starting to build and push image"
                    sh '''
                            mvn jib:build
                        '''
                }
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'next'
            }
            environment {
                ENV = 'stg'
            }
            steps {
                script {
                    echo "Starting to deploy to EKS"
                    sh '''
                            envsubst < values-${ENV}.yaml | helm upgrade --install twinci-service-${ENV} --namespace=twinci-${ENV} -f - /var/lib/jenkins/template/twinci-aws-application
                        '''
                }
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'develop'
            }
            environment {
                ENV = 'prd'
            }
            steps {
                script {
                    echo "Starting to deploy to EKS"
                    sh '''
                            envsubst < values-prd.yaml | helm upgrade --install twinci-service --namespace=twinci -f - /var/lib/jenkins/template/twinci-aws-application
                        '''
                }
            }
        }
    }
    post {
        success {
            slackSend color: "good", message: "SUCCESSFUL: ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
        }

        failure {
            slackSend color: "danger", message: "FAILED: ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
        }

        always {
            cleanWs()
        }
    }
}
