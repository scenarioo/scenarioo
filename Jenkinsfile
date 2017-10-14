def gradle(tasks) {
	 sh "./gradlew --info --no-daemon $tasks"
}

def getEncodedBranchName() {
	 String branchName = "${env.BRANCH_NAME}"
	 return branchName.replace('/', '-').replace('#', '')
}

def reportJenkinsSummary(summaryFile, title, messageHtml) {
    def contentHtml = "<h2>${title}</h2> <div>${messageHtml}</div>"
    def contentCss = ""
    def overruleUglyPluginStyle = ".summary_report_table {border:none;} .summary_report_table td {border:none;}"
    def htmlSnippet = "<style>${overruleUglyPluginStyle} ${contentCss}</style> ${contentHtml}"
    sh "echo '<section><table><tr><td><![CDATA[ ${htmlSnippet} ]]></td></tr></table></section>' > ${summaryFile}"
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
                    def demoUrl = "http://demo.scenarioo.org/scenarioo-${encodedBranchName}"
                    reportJenkinsSummary("deploy.jenkins-summary.xml",
                        "Scenarioo Demo Deployed",
                        "Deployed to "
                            + "<a target=\"_blank\" href=\"${demoUrl}\">"
                            + "${demoUrl}</a>")
                }
                catch (e) {
                    reportJenkinsSummary("deploy-failed.jenkins-summary.xml",
                            "Scenarioo Demo Deployment Failed",
                            "<b><font color=\"#ff3333\">Deployment failed!</font></b>")
                }

            }
        }

        stage('Run e2e tests') {
            ansiColor('xterm') {

                try {
                         sh "./ci/runE2ETests.sh --branch=${encodedBranchName}"
                } finally {
                         sh "./ci/deploySelfDocu.sh --branch=${encodedBranchName}"
                         $selfDocuUrl = "http://demo.scenarioo.org/scenarioo-${encodedBranchName}?branch=${encodedBranchName}"
                         reportJenkinsSummary("deploySelfDocu.jenkins-summary.xml",
                                              "Scenarioo Self Docu",
                                              "E2E Test Reports at "
                                              + "<a target=\"_blank\" href=\"${selfDocuUrl}\">"
                                              + "${selfDocuUrl}</a>")
                         junit 'scenarioo-client/test-reports/*.xml'
                }

            }
        }

	}
}
