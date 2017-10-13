def gradle(tasks) {
	 sh "./gradlew --info --no-daemon $tasks"
}

def getEncodedBranchName() {
	 String branchName = "${env.BRANCH_NAME}"
	 return branchName.replace('/', '-').replace('#', '')
}

properties([
	disableConcurrentBuilds(),
	pipelineTriggers([
		[$class: 'GitHubPushTrigger']
	])
])

timestamps {
	node {
        stage('Checkout') {
            checkout scm
        }

        def encodedBranchName = getEncodedBranchName()

        stage('Build and unit test') {
            ansiColor('xterm') {

                try {
                     gradle 'clean build'
                } finally {
                     junit '**/build/test-results/test/*.xml, scenarioo-client/TEST*.xml'
                }

            }
        }

        stage('Package') {
            gradle 'distZip'
            archiveArtifacts 'scenarioo-server/build/libs/scenarioo-*.war, LICENSE.txt, README.md, ' +
                        'scenarioo-docu-generation-example/build/scenarioDocuExample/, scenarioo-validator/build/distributions/*'
        }

        stage('Deploy') {
            ansiColor('xterm') {

                sh "./ci/deploy.sh --branch=${encodedBranchName}"

            }
        }

        stage('Run e2e tests') {
            ansiColor('xterm') {

                try {
                         sh "./ci/runE2ETests.sh --branch=${encodedBranchName}"
                } finally {
                         sh "./ci/deploySelfDocu.sh --branch=${encodedBranchName}"
                         junit 'scenarioo-client/test-reports/*.xml'
                }

            }
        }

	}
}
