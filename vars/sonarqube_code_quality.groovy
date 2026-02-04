def call() {
    // Clear all proxy settings before calling SonarQube
    withEnv([
        'NO_PROXY=13.235.104.170,localhost,127.0.0.1,*.local',
        'no_proxy=13.235.104.170,localhost,127.0.0.1,*.local',
        'HTTP_PROXY=',
        'HTTPS_PROXY=',
        'http_proxy=',
        'https_proxy='
    ]) {
        script {
            try {
                echo "⏳ Waiting for SonarQube Quality Gate..."
                timeout(time: 5, unit: 'MINUTES') {
                    def qg = waitForQualityGate(abortPipeline: false)
                    
                    if (qg.status != 'OK') {
                        echo "⚠️ Quality Gate status: ${qg.status}"
                        echo "Quality Gate failed, but continuing build..."
                    } else {
                        echo "✅ Quality Gate passed!"
                    }
                }
            } catch (Exception e) {
                echo "❌ Quality Gate check error: ${e.message}"
                echo "Continuing build anyway..."
                currentBuild.result = 'UNSTABLE'
            }
        }
    }
}
