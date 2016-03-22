var scenarioo = require('scenarioo-js');

function describeUseCaseE(jasmineDescribeFunction, useCaseName, additionalProperties, callback) {
    if (!callback) {
        callback = additionalProperties;
    }
    return jasmineDescribeFunction(useCaseName, function () {
        return callback();
    });
}

function describeScenarioE(jasmineItFunction, scenarioName, additionalProperties, callback) {
    if (!callback) {
        callback = additionalProperties;
    }
    jasmineItFunction(scenarioName, function () {
        return callback();
    });
}

global.describeUseCaseE = describeUseCaseE.bind(undefined, describe);
global.describeScenarioE = describeScenarioE.bind(undefined, it);
