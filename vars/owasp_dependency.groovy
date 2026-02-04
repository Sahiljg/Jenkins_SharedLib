def call() {
    withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
        dependencyCheck additionalArguments: """
            --nvdApiKey ${NVD_API_KEY}
            --format HTML
            --format XML
        """, odcInstallation: 'DP-Check'
        
        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
    }
}
