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

        if (env.BRANCH_NAME == "develop" || env.BRANCH_NAME == "master" || env.BRANCH_NAME.startsWith("release")) {
            def branchNameTokens = env.BRANCH_NAME.tokenize('/')
            def docsVersionFolder = env.BRANCH_NAME.startsWith("release/") ? branchNameTokens[1] : env.BRANCH_NAME

            stage("Publish Markdown Docs ${docsVersionFolder}") {
                ansiColor('xterm') {
                    withCredentials([usernameColonPassword(credentialsId: 'efe50290-8cf4-4d93-9835-3e5774a129ff', variable: 'GIT_USERPASS')]) {
                        sh "./ci/publishGitbookMarkdownDocu.sh --docsDistributionFolder=${docsVersionFolder}"
                        reportJenkinsSummaryGitbookMarkdownDocu(docsVersionFolder)
                    }
                }
            }
        }

	}

}
