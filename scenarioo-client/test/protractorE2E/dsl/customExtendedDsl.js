/**
 *
 * An extended example of a custom DSL.
 *
 * describeUseCase and describeScenario take an additional argument in order to
 * eliminate boilerplate code in your tests.
 */
var scenarioo = require('scenarioo-js');

function describeUseCaseE(jasmineDescribeFunction, useCaseName, additionalProperties, callback) {
    if (!callback) {
        callback = additionalProperties;
    }
    return jasmineDescribeFunction(useCaseName, function () {
        beforeAll(function () {
            scenarioo.getUseCaseContext().setDescription(additionalProperties.description);
            scenarioo.getUseCaseContext().addLabels(additionalProperties.labels);
        });
        return callback();
    });
}

function describeScenarioE(jasmineItFunction, scenarioName, additionalProperties, callback) {
    if (!callback) {
        callback = additionalProperties;
    }
    jasmineItFunction(scenarioName, function () {
        scenarioo.getScenarioContext().setDescription(additionalProperties.description);
        scenarioo.getScenarioContext().addLabels(additionalProperties.labels);
        return callback();
    });
}

global.describeUseCaseE = describeUseCaseE.bind(undefined, describe);
global.describeScenarioE = describeScenarioE.bind(undefined, it);
