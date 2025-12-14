pipeline {
    agent any
    
    stages {
        stage('Build Contracts') {
            steps {
                dir('events-contract') {
                    sh './.mvnw clean install -DskipTests'
                }
                dir('car-rental-api') {
                    sh './.mvnw clean install -DskipTests'
                }
            }
        }
        
        stage('Build Services') {
            parallel {
                stage('Build car-rental') {
                    steps {
                        dir('car-rental') {
                            sh './.mvnw clean package -DskipTests'
                        }
                    }
                }

                stage('Build analytics-service') {
                    steps {
                        dir('analytics-service') {
                            sh './.mvnw clean package -DskipTests'
                        }
                    }
                }
                stage('Build notification-service') {
                    steps {
                        dir('notification-service') {
                            sh './.mvnw clean package -DskipTests'
                        }
                    }
                }
                stage('Build grpc-pricing') {
                    steps {
                        dir('grpc-pricing') {
                            sh './.mvnw clean package -DskipTests'
                        }
                    }
                }
            
            }
        }
        
        stage('Docker Build') {
            steps {
                sh 'docker-compose build --no-cache'
            }
        }
        
        stage('Deploy') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
                sleep(time: 30, unit: 'SECONDS')
            }
        }
        
        stage('Health Check') {
            steps {
                sh '''
                curl -f http://car-rental:8080/actuator/health || exit 1
                curl -f http://notification-service:8083/actuator/health || exit 1
                echo "Все сервисы работают"
                '''
            }
        }
    }
    
    post {
        success {
            echo 'Сборка успешна'
            echo 'Доступные сервисы:'
            echo '1. Приложение: http://localhost:8080'
            echo '2. Grafana: http://localhost:3000 (admin/admin)'
            echo '3. Zipkin: http://localhost:9411'
            echo '4. Jenkins: http://localhost:8081'
        }
    }
}