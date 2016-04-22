/**
 * Definitions of allowed labels to be used in our e2e tests for scenarioo
 */

/**
 * Define all the labels that are allowed to be used on use cases
 */
scenariooDslConfig.useCaseLabels = {};

/**
 * Define all the labels that are allowed to be used on scenarios
 */
scenariooDslConfig.scenarioLabels = {
    'happy': 'Happy case scenarios that document most trivial flows through usecases without errors',
    'validation': 'Special scenario that tests how the system behaves if the user enters invalid data. But the test will still finalize the usecase by correcting the invalid data and to test that after validation exceptions the user can still finalize the user scenario to reach his goal.',
    'exception': 'Exception path scenario that tests that the system behaves as expected in fault conditions, e.g. when the bakend returns an error.',
    'incomplete': 'Scenario that is not yet comletely implemented, only testing part of the user scenario, e.g. only first steps of a long wizard flow, for test scenarios that are work in progress'
};
