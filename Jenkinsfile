pipeline {
    agent any
    
    stages {
       
        stage('Build') {
            stages {
                stage('Build events-contract') {
                    steps {
                        dir('events-contract') {
                            sh './mvnw clean install -DskipTests -B'
                        }
                    }
                }
                stage('Build car-rental-api') {
                    steps {
                        dir('car-rental-api') {
                            sh './mvnw clean install -DskipTests -B'
                        }
                    }
                }
                stage('Build services') {
                    parallel {
                        stage('car-rental') {
                            steps {
                                dir('car-rental') {
                                    sh './mvnw clean package -DskipTests -B'
                                }
                            }
                        }
                        stage('notification-service') {
                            steps {
                                dir('notification-service') {
                                    sh './mvnw clean package -DskipTests -B'
                                }
                            }
                        }
                        stage('grpc-pricing') {
                            steps {
                                dir('grpc-pricing') {
                                    sh './mvnw clean package -DskipTests -B'
                                }
                            }
                        }
                        stage('analytics-service') {
                            steps {
                                dir('analytics-service') {
                                    sh './mvnw clean package -DskipTests -B'
                                }
                            }
                        }
                    }
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                sh 'docker compose build car-rental notification-service grpc-pricing analytics-service'
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    docker compose down -v --remove-orphans || true

                    docker compose up -d --force-recreate car-rental notification-service grpc-pricing analytics-service
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
            sh 'docker compose logs | tail -n 50 || true'
        }
    }
}
