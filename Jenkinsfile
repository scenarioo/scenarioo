def gradle(tasks) {
	 sh "./gradlew --info --no-daemon $tasks"
}

def getEncodedBranchName() {
	 String branchName = "${env.BRANCH_NAME}"
	 return branchName.replace('/', '-').replace('#', '')
}

def reportJenkinsSummary(summaryFile, title, messageHtml) {
    // def styling = "<style>p.first {color:green;} p.second {color:blue;}"
    def contentHtml = "<h2>${title}</h2> <div>${messageHtml}</div>"
    def injectedTableStyle = "width=\"; border:none\""
    def wrappedHtml = "<table><tr><td ${injectedTableStyle}><![CDATA[${contentHtml}]]></td></tr></table>"
    sh "echo '<section>${snippetHtml}</section>' > ${summaryFile}"
    archive summaryFile
    step([$class: 'ACIPluginPublisher', name: summaryFile, shownOnProjectPage: true])
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

                reportJenkinsSummary('build.jenkins-summary.xml',
                                                'Build started',
                                                'Deployment expected on <a target=\"_blank\" '
                                                    + 'href=\"http://demo.scenarioo.org/scenarioo-${encodedBranchName}\">'
                                                    + 'http://demo.scenarioo.org/scenarioo-${encodedBranchName}'
                                                    + '</a>')

                reportJenkinsSummary('deploySelfDocu.jenkins-summary.xml',
                                                'Scenarioo Self Docu',
                                                'Self Docu at <a target=\"_blank\" '
                                                    + 'href=\"http://demo.scenarioo.org/scenarioo-${encodedBranchName}\">'
                                                    + 'http://demo.scenarioo.org/scenarioo-${encodedBranchName}'
                                                    + '</a>')


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

                try {
                    sh "./ci/deploy.sh --branch=${encodedBranchName}"
                    reportJenkinsSummary('deploy.jenkins-summary.xml',
                        'Scenarioo Demo Deployment',
                        'Deployed to <a target=\"_blank\" '
                            + 'href=\"http://demo.scenarioo.org/scenarioo-${encodedBranchName}\">'
                            + 'http://demo.scenarioo.org/scenarioo-${encodedBranchName}'
                            + '</a>')
                }
                catch (e) {
                    reportJenkinsSummary('deploy.jenkins-summary.xml',
                            'Scenarioo Demo Deployment',
                            '<b><font color="#ff0000">Deployment failed!</font></b>')
                }




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
