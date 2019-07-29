import {
    IScenario,
    IScenarioDetails,
    IScenarioStatistics,
    IUseCase,
} from '../../generated-types/backend-types';
import {PageWithSteps} from './PageWithSteps';

export class ScenarioDetails {
    pagesAndSteps: PageWithSteps[];
    scenario: IScenario;
    scenarioStatistics: IScenarioStatistics;
    useCase: IUseCase;

    constructor(source: IScenarioDetails) {
        this.scenario = source.scenario;
        this.scenarioStatistics = source.scenarioStatistics;
        this.useCase = source.useCase;
        this.pagesAndSteps = source.pagesAndSteps.map((value) => new PageWithSteps(value));
    }
}
