def gradle(tasks) {
	 sh "./gradlew --info $tasks"
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
	 }
}
