def call() {
    withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
        try {
            echo "🔍 Running OWASP Dependency Check with NVD API..."
            
            // Pass the key via environment variable to avoid string interpolation warning
            sh '''
                dependency-check.sh \
                    --nvdApiKey "${NVD_API_KEY}" \
                    --format HTML \
                    --format XML \
                    --scan . \
                    --project "wanderlust" \
                    --out ./dependency-check-report
            '''
            
            dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            
            echo "✅ OWASP Dependency Check completed successfully"
        } catch (Exception e) {
            echo "⚠️ OWASP Dependency Check failed: ${e.message}"
            currentBuild.result = 'UNSTABLE'
        }
    }
}
