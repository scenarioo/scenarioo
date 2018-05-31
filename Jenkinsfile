def gradle(tasks) {
	 sh "./gradlew --info -s --no-daemon $tasks"
}

def getEncodedBranchName() {
	 String branchName = "${env.BRANCH_NAME}"
	 return branchName.replace('/', '-').replace('#', '')
}

/**
 * Output summary message to jenkins build page
 * with nice Scenarioo logo icon and styling
 * using https://wiki.jenkins.io/display/JENKINS/Summary+Display+Plugin
 */
def reportJenkinsSummary(summaryFile, contentHtml) {
    def scenariooIconUrl = "https://raw.githubusercontent.com/scenarioo/scenarioo/develop/scenarioo-client/resources/LogoScenariooBlackQuadraticSmall.png"
    def scenariooIconHtml = "<img src=\"${scenariooIconUrl}\" style=\"width: 48px; height: 48px; \" class=\"icon-scenarioo icon-xlg\">"
    def contentHtmlWithIcon = "<table><tbody><tr><td>${scenariooIconHtml}</td><td style=\"vertical-align:middle\">${contentHtml}</td></tr></tbody></table>"
    def contentCss = ""
    def overruleUglyPluginStyleCss = ".summary_report_table {border:none;border-spacing:0px;} .summary_report_table td {border:none;border-spacing:0px;}"
    def htmlSnippet = "<style>${overruleUglyPluginStyleCss} ${contentCss}</style> ${contentHtmlWithIcon}"
    sh "echo '<section><table><tr><td width=\"; margin:0px; padding:0px;\"><![CDATA[ ${htmlSnippet} ]]></td></tr></table></section>' > ${summaryFile}"
    archive summaryFile
    step([$class: 'ACIPluginPublisher', name: summaryFile, shownOnProjectPage: true])
}

/**
 * Output summary message on jenkins build page
 * with the link to scenarioo reports (self docu)
 * for current build run
 */
def reportJenkinsSummaryScenariooReports(scenariooUrl, branchId, buildId) {
    def scenariooReportUrl = "${scenariooUrl}/#/?branch=${branchId}&build=${buildId}"
    echo "See Scenarioo E2E Test Reports for this build: ${scenariooReportUrl}"
    def title = "<h2>Scenarioo Reports</h2>"
    def summary = "<a target=\"_blank\" href=\"${scenariooReportUrl}\">Scenarioo E2E Test Reports for this build</a>"
    reportJenkinsSummary("scenarioo-reports.jenkins-summary.xml", "${title} ${summary}")
}

/**
 * Output summary message on jenkins build page
 * with the link to updated gitbook markdown docu
 * for current build run
 */
def reportJenkinsSummaryGitbookMarkdownDocu(docuVersionName) {
    def markdownDocuUrl = "http://www.scenarioo.org/docs/${docuVersionName}/"
    echo "Scenarioo Gitbook Markdown Docu on Webpage updated: ${markdownDocuUrl}"
    def title = "<h2>Published Documentation on Webpage</h2>"
    def summary = "<a target=\"_blank\" href=\"${markdownDocuUrl}\">Scenarioo Gitbook Docs for ${docuVersionName}</a>"
    reportJenkinsSummary("scenarioo-gitbook-markdown-docu.jenkins-summary.xml", "${title} ${summary}")
}


properties([
	disableConcurrentBuilds(),
	pipelineTriggers([
		[$class: 'GitHubPushTrigger']
	]),
	buildDiscarder(logRotator(
	    artifactDaysToKeepStr: '10',
	    artifactNumToKeepStr: '5',
	    daysToKeepStr: '30',
	    numToKeepStr: '20'
	))
])

timestamps {

	node {

        stage('Checkout') {
            checkout scm
            sh "git clean -x -d -f"
        }

        def encodedBranchName = getEncodedBranchName()
/*
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
            archiveArtifacts ("scenarioo-server/build/libs/scenarioo-*.war, LICENSE.txt, README.md, "
                              + "scenarioo-docu-generation-example/build/scenarioDocuExample/, "
                              + "scenarioo-validator/build/distributions/*")
        }

        stage('Deploy') {
            ansiColor('xterm') {

                try {

                    withCredentials([usernameColonPassword(credentialsId: 'SCENARIOO_TOMCAT', variable: 'TOMCAT_USERPASS')]) {
                        sh "./ci/deploy.sh --branch=${encodedBranchName}"
                        def demoUrl = "http://demo.scenarioo.org/scenarioo-${encodedBranchName}"
                        reportJenkinsSummary("deploy.jenkins-summary.xml",
                            "<h2>Scenarioo Demo Deployed</h2>"
                            + "Deployed to "
                            + "<a target=\"_blank\" href=\"${demoUrl}\">"
                            + "${demoUrl}</a>")
                    }
                }
                catch (e) {
                    reportJenkinsSummary("deploy-failed.jenkins-summary.xml",
                            "<h2>Scenarioo Demo Deployment Failed</h2>"
                            + "<b><font color=\"#ff3333\">Deployment failed!</font></b>")
                    // Fail the entire build if the deployment fails
                    throw e;
                }

            }
        }

        stage('Run e2e tests') {
            ansiColor('xterm') {

                try {
                         sh "./ci/runE2ETests.sh --branch=${encodedBranchName}"
                } finally {
                    junit 'scenarioo-client/test-reports/*.xml'
                    withCredentials([usernameColonPassword(credentialsId: 'SCENARIOO_TOMCAT', variable: 'TOMCAT_USERPASS')]) {
                         // Only for the master branch the self docu is deployed to scenarioo-master
                         // for all others: to scenarioo-develop
                         def docuDeploymentScenariooInstance = encodedBranchName == "master" ? "master" : "develop"
                         def scenariooUrl = "http://demo.scenarioo.org/scenarioo-${docuDeploymentScenariooInstance}"
                         sh "./ci/deploySelfDocu.sh --branch=${encodedBranchName}"
                         reportJenkinsSummaryScenariooReports(scenariooUrl, "scenarioo-${encodedBranchName}", "build-${env.BUILD_NUMBER}")
                    }
                }

            }
        }
*/
        def docsVersionFolder = "develop"
        // if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master" || env.BRANCH_NAME.startsWith("release")) {
            def (branchPrefix, releaseBranchVesion) = env.BRANCH_NAME.tokenize('/')
            // def docsVersionFolder = env.BRANCH_NAME.startsWith("release/") ? releaseBranchVesion : env.BRANCH_NAME

            stage("Publish Markdown Docu ${env.BRANCH_NAME}") {
                ansiColor('xterm') {
                    withCredentials([usernameColonPassword(credentialsId: 'scenarioo-ci', variable: 'GIT_USERPASS')]) {
                        sh "./ci/publishGitbookMarkdownDocu.sh --docsDistributionFolder=${docsVersionFolder}"
                        reportJenkinsSummaryGitbookMarkdownDocu(docsVersionFolder)
                    }
                }
            }
        // }

	}
}
