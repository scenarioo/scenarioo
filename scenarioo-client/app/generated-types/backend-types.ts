/* tslint:disable */

export interface IConfiguration {
    defaultBranchName: string;
    defaultBuildName: string;
    aliasForMostRecentBuild: string;
    aliasForLastSuccessfulBuild: string;
    buildStatusForSuccessfulBuilds: string;
    scenarioPropertiesInOverview: string;
    elasticSearchEndpoint: string;
    elasticSearchClusterName: string;
    applicationName: string;
    applicationInformation: string;
    branchSelectionListOrder: string;
    buildstates: { [index: string]: string };
    diffImageColor: string;
    createLastSuccessfulScenarioBuild: boolean;
    expandPagesInScenarioOverview: boolean;
    branchAliases: IBranchAlias[];
    comparisonConfigurations: IComparisonConfiguration[];
    labelConfigurations: { [index: string]: ILabelConfiguration };
    customObjectTabs: ICustomObjectTab[];
}

export interface IApplicationStatus {
    configuration: IConfiguration;
    documentationDataDirectory: string;
    searchEngineStatus: ISearchEngineStatus;
    version: IApplicationVersion;
}

export interface IBranchAlias {
    name: string;
    referencedBranch: string;
    description: string;
}

export interface IFlatLabelConfiguration extends ILabelConfiguration {
    name: string;
}

export interface IApplicationVersion {
    version: string;
    buildDate: string;
    apiVersion: string;
    aggregatedDataFormatVersion: string;
    documentationVersion: string;
}

export interface IBuildImportSummary {
    identifier: IBuildIdentifier;
    buildDescription: IBuild;
    status: IBuildImportStatus;
    statusMessage: string;
    importDate: Date;
    buildStatistics: IBuildStatistics;
}

export interface IBuildDiffInfo extends IStructureDiffInfo<string, IUseCase> {
    baseBuild: IBuildIdentifier;
    compareBuild: IBuildIdentifier;
    status: IComparisonCalculationStatus;
    calculationDate: Date;
    baseBuildDate: Date;
}

export interface IScenarioDiffInfo extends IStructureDiffInfo<number, IStepInfo> {
}

export interface IStepDiffInfo extends IAbstractDiffInfo {
    index: number;
    pageName: string;
    pageOccurrence: number;
    stepInPageOccurrence: number;
    comparisonScreenshotName: string;
}

export interface IUseCaseDiffInfo extends IStructureDiffInfo<string, IScenarioSummary> {
}

export interface ICustomObjectTabTree {
    tree: IObjectTreeNode<IObjectReference>[];
}

export interface IObjectDescription extends ISerializable, IDetailable {
    name: string;
    type: string;
}

export interface IObjectIndex {
    object: IObjectDescription;
    referenceTree: IObjectTreeNode<IObjectReference>;
}

export interface IStepReference {
    stepDetailUrl: string;
    screenShotUrl: string;
}

export interface IUseCaseScenarios {
    useCase: IUseCase;
    scenarios: IScenarioSummary[];
}

export interface IScenarioDetails {
    scenario: IScenario;
    scenarioStatistics: IScenarioStatistics;
    useCase: IUseCase;
    pagesAndSteps: IPageWithSteps[];
}

export interface ISearchResponse {
    searchTree: ISearchTree;
    errorMessage: string;
}

export interface ISearchEngineStatus {
    running: boolean;
    endpointConfigured: boolean;
    endpoint: string;
}

export interface IUseCaseSummary {
    status: string;
    name: string;
    description: string;
    numberOfScenarios: number;
    labels: ILabels;
}

export interface IBranchBuilds {
    branch: IBranch;
    builds: IBuildLink[];
    // TODO: What is the difference?
    alias: boolean;
    isAlias: boolean;
}

export interface ISketchIds {
    scenarioSketchId: string;
    stepSketchId: string;
}

export interface IComparisonConfiguration {
    name: string;
    baseBranchName: string;
    comparisonBranchName: string;
    comparisonBuildName: string;
}

export interface ILabelConfiguration {
    backgroundColor: string;
    foregroundColor: string;
}

export interface ICustomObjectTab {
    id: string;
    tabTitle: string;
    objectTypesToDisplay: string[];
    customObjectDetailColumns: ICustomObjectDetailColumn[];
}

export interface IBuildIdentifier {
    branchName: string;
    buildName: string;
}

export interface IBuild extends ISerializable, IDetailable {
    name: string;
    revision: string;
    date: Date;
    status: string;
}

export interface IBuildStatistics {
    numberOfFailedScenarios: number;
    numberOfSuccessfulScenarios: number;
    numberOfSuccessfulUseCases: number;
    numberOfFailedUseCases: number;
}

export interface IAbstractDiffInfo {
    changeRate: number;
}

export interface IObjectTreeNode<T> extends ISerializable, IDetailable {
    item: T;
    children: IObjectTreeNode<any>[];
}

export interface IObjectReference extends ISerializable {
    name: string;
    type: string;
}

export interface ISerializable {
}

export interface IDetailable {
    details: { [index: string]: any };
}

export interface IUseCase extends ISerializable, ILabelable, IDetailable {
    name: string;
    description: string;
    status: string;
}

export interface IScenarioSummary {
    scenario: IScenario;
    numberOfSteps: number;
}

export interface IScenario extends ISerializable, ILabelable, IDetailable {
    name: string;
    description: string;
    status: string;
}

export interface IScenarioStatistics extends ISerializable {
    numberOfPages: number;
    numberOfSteps: number;
}

export interface IPageWithSteps {
    page: IPageSummary;
    steps: IStepDescription[];
}

export interface ISearchTree {
    results: IObjectTreeNode<IObjectReference>;
    hits: number;
    totalHits: number;
    searchRequest: ISearchRequest;
}

export interface ILabels {
    labels: string[];
    empty: boolean;
}

export interface IBranch extends ISerializable, IDetailable {
    name: string;
    description: string;
}

export interface IBuildLink extends ISerializable {
    displayName: string;
    linkName: string;
    build: IBuild;
}

export interface ICustomObjectDetailColumn {
    columnTitle: string;
    propertyKey: string;
}

export interface IStructureDiffInfo<A, R> extends IAbstractDiffInfo {
    name: string;
    added: number;
    changed: number;
    removed: number;
    addedElements: A[];
    removedElements: R[];
}

export interface IStepInfo {
    stepLink: IStepLink;
    stepDescription: IStepDescription;
}

export interface ILabelable {
    labels: ILabels;
}

export interface IPageSummary {
    name: string;
    pageOccurrence: number;
}

export interface IStepDescription extends ISerializable, ILabelable, IDetailable {
    index: number;
    title: string;
    status: string;
    screenshotFileName: string;
}

export interface ISearchRequest {
    buildIdentifier: IBuildIdentifier;
    q: string;
}

export interface IStepLink {
    useCaseName: string;
    scenarioName: string;
    pageName: string;
    pageOccurrence: number;
    stepInPageOccurrence: number;
    pageIndex: number;
    stepIndex: number;
    stepIdentifierForObjectRepository: string;
}

export type IBuildImportStatus = "UNPROCESSED" | "QUEUED_FOR_PROCESSING" | "PROCESSING" | "SUCCESS" | "FAILED" | "OUTDATED";

export type IComparisonCalculationStatus = "QUEUED_FOR_PROCESSING" | "PROCESSING" | "SKIPPED" | "SUCCESS" | "FAILED";
