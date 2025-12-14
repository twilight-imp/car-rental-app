pipeline {
    agent any
    
    stages {
       
        stage('Build') {
            parallel {
                stage('events-contract') {
                    steps {
                        dir('events-contract') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
                stage('car-rental-api') {
                    steps {
                        dir('car-rental-api') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
                stage('car-rental') {
                    steps {
                        dir('car-rental') {
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }
                stage('notification-service') {
                    steps {
                        dir('notification-service') {
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }
                stage('grpc-pricing') {
                    steps {
                        dir('grpc-pricing') {
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }
                stage('analytics-service') {
                    steps {
                        dir('analytics-service') {
                            sh './mvnw clean package -DskipTests'
                        }
                    }
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                sh '''
                    docker compose build car-rental notification-service grpc-pricing analytics-service
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    docker compose down
                    docker compose up -d car-rental notification-service grpc-pricing analytics-service
                '''
            }
        }
        
    }
    
    post {
        success {
            echo 'Build successful'
        }
        failure {
            echo 'Build failed'
            sh 'docker compose logs --tail=20'
        }
    }
}