def gradle(tasks) {
	 sh "./gradlew --info $tasks"
}

timestamps {
	 node {
		  stage('Checkout') {
				checkout scm
		  }

		  stage('Build and unit test'){
				try {
					 gradle 'clean build'
				} finally {
					 junit '**/build/test-results/test/*.xml, scenarioo-client/TEST*.xml'
				}
		  }

		  stage('Package') {
				gradle 'distZip'
		  }
	 }
}
