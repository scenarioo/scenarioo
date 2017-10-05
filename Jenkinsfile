def gradle(tasks) {
	 sh "./gradlew --info --no-daemon $tasks"
}

timestamps {
	 node {
		  stage('Checkout') {
				checkout scm
		  }

		  stage('Build and unit test') {
				try {
					 gradle 'clean build'
				} finally {
					 junit '**/build/test-results/test/*.xml, scenarioo-client/TEST*.xml'
				}
		  }

		  stage('Package') {
				gradle 'distZip'
				archiveArtifacts 'scenarioo-server/build/libs/scenarioo-*.war, LICENSE.txt, README.md, ' +
						  'scenarioo-docu-generation-example/build/scenarioDocuExample/, scenarioo-validator/build/distributions/*'
		  }

		  lock('deploy-and-e2e-tests') {
				stage('Deploy') {
					 sh "./ci/deploy.sh --branch=${env.BRANCH_NAME}"
				}

				stage('Run e2e tests') {
					 try {
						  sh "./ci/runE2ETests.sh --branch=${env.BRANCH_NAME}"
					 } finally {
						  junit 'scenarioo-client/test-reports/*.xml'
					 }
				}
		  }

		  stage('Deploy self docu') {
				sh "./ci/deploySelfDocu.sh --branch=${env.BRANCH_NAME}"
		  }
	 }
}
