/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

angular.module('scenarioo.services').service('TestData', function () {
    return {
        CONFIG: {
            'testDocumentationDirPath': 'webtestDocuContentExample',
            'defaultBuildName': 'current',
            'scenarioPropertiesInOverview': 'userProfile, configuration',
            'applicationInformation': 'This is my personal copy of Scenarioo :-)',
            'buildstates': {
                BUILD_STATE_FAILED: 'label-important',
                BUILD_STATE_SUCCESS: 'label-success',
                BUILD_STATE_WARNING: 'label-warning'
            },
            'defaultBranchName': 'trunk'
        },

        BRANCHES: [
            {'branch': {'description': 'Just an example development branch from example docu generation example.', 'name': 'release-branch-2013-11-14'}, 'builds': [
                {'linkName': 'example-build', 'build': {'details': {}, 'status': 'success', 'revision': '123456', 'date': 1385284268394, 'name': 'example-build'}}
            ]},
            {'branch': {'description': 'Just an example development branch from example docu generation example.', 'name': 'release-branch-2014-01-16'}, 'builds': [
                {'linkName': 'example-build', 'build': {'details': {}, 'status': 'success', 'revision': '123456', 'date': 1385284268394, 'name': 'example-build'}}
            ]},
            {'branch': {'description': 'Just an example development branch from example docu generation example.', 'name': 'trunk'}, 'builds': [
                {'linkName': 'example-build', 'build': {'details': {}, 'status': 'success', 'revision': '123456', 'date': 1385284268394, 'name': 'example-build'}}
            ]}
        ],

        BUILD_IMPORT_STATES: [
            {
                'buildDescription': {'details': {}, 'revision': '123456', 'status': 'success', 'date': 1388879915785, 'name': 'example-build'},
                'importDate': 1388620336537,
                'status': 'SUCCESS',
                'identifier': {'branchName': 'example-branch', 'buildName': 'example-build'}
            }
        ],

        USECASES: [
            {'useCase': {'details': {'webtestClass': 'org.scenarioo.uitest.example.testcases.FindPageUITest'}, 'status': 'success', 'description': 'User wants to search for a page and read it.', 'name': 'Find Page'}, 'scenarios': [
                {'details': {'userRole': 'unauthenticated'}, 'status': 'success', 'description': 'User enters text that is not found in pages content.', 'calculatedData': {'numberOfPages': 1, 'numberOfSteps': 3}, 'name': 'find_page_no_result'},
                {'details': {'userRole': 'unauthenticated'}, 'status': 'success', 'description': 'User enters some text and finds multiple pages that contain this text.', 'calculatedData': {'numberOfPages': 1, 'numberOfSteps': 5}, 'name': 'find_page_with_text_on_page_from_multiple_results'},
                {'details': {'userRole': 'unauthenticated'}, 'status': 'success', 'description': 'User enters page title that is ambiguous but matches directly a page, on the page he sees the list of other meanings, and can navigate to the page he meant.', 'calculatedData': {'numberOfPages': 1, 'numberOfSteps': 7}, 'name': 'find_page_with_title_ambiguous_navigate_to_other_meaning'},
                {'details': {'userRole': 'unauthenticated'}, 'status': 'success', 'description': 'User enters exact page title and finds it directly.', 'calculatedData': {'numberOfPages': 1, 'numberOfSteps': 3}, 'name': 'find_page_with_title_direct'}
            ]}
        ],

        SCENARIO: {
            'useCase': {
                'details': {
                    'webtestClass': 'org.scenarioo.uitest.example.testcases.FindPageUITest'
                },
                'status': 'success',
                'description': 'User wants to search for a page and read it.',
                'name': 'Find Page'
            },
            'pagesAndSteps': [
                {'page': {'details': {}, 'name': 'startSearch.jsp'}, 'steps': [
                    {'details': {'url': 'http://www.wikipedia.org'}, 'status': 'success', 'title': 'Wikipedia Suche', 'screenshotFileName': '000.png', 'index': 0},
                    {'details': {'url': 'http://www.wikipedia.org'}, 'status': 'success', 'title': 'Wikipedia Suche', 'screenshotFileName': '001.png', 'index': 1}
                ]},
                {'page': {'details': {}, 'name': 'searchResults.jsp'}, 'steps': [
                    {'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'}, 'status': 'success', 'title': 'Search results', 'screenshotFileName': '002.png', 'index': 2},
                    {'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'}, 'status': 'success', 'title': 'Search results', 'screenshotFileName': '003.png', 'index': 3},
                    {'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'}, 'status': 'success', 'title': 'Search results', 'screenshotFileName': '004.png', 'index': 4}
                ]}
            ],
            'scenario': {
                'details': {
                    'userRole': 'unauthenticated'
                },
                'status': 'success',
                'description': 'User enters some text and finds multiple pages that contain this text.',
                'calculatedData': {
                    'numberOfPages': 1,
                    'numberOfSteps': 5
                },
                'name': 'find_page_with_text_on_page_from_multiple_results'
            }
        },

        /**
         * Based on
         * http://localhost:8080/scenarioo/rest/branch/example-branch/build/example-build/usecase/Find%20Page/scenario/find_page_with_text_on_page_from_multiple_results/pageName/searchResults.jsp/pageOccurrence/0/stepInPageOccurrence/0
         * with simplified metadata.
         */
        STEP: {
            'stepStatistics': {
                'totalNumberOfStepsInScenario': 5,
                'totalNumberOfStepsInPageOccurrence': 2,
                'totalNumberOfPagesInScenario': 3
            },
            'step': {
                'stepDescription': {
                    'details': {
                        'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'
                    },
                    'status': 'success',
                    'title': 'Search results',
                    'screenshotFileName': '002.png',
                    'index': 2
                },
                'metadata': {
                    'details': {
                        'simulationConfiguration': {
                            'details': {
                                'overridenConfigModules': [
                                ],
                                'defaultConfigModules': [
                                ]
                            },
                            'name': 'ambiguoties_config',
                            'type': 'configuration'
                        },
                        'callTree': {
                            'details': {},
                            'item': {
                                'details': {},
                                'name': 'http://www.wikipedia.org',
                                'type': 'httpCall'
                            }
                        }
                    },
                    'visibleText': 'Bla bla bla bla bla ... This is the visible text as generated from dummy test.'
                },
                'html': {
                    'htmlSource': '<html>\n<head>\n</head>\n<body>\n   <p>just some dummy html code</p>\n</body>\n</html>'
                },
                'page': {
                    'details': {},
                    'name': 'searchResults.jsp'
                }
            },
            'stepNavigation': {
                'stepIndex': 2,
                'pageName': 'searchResults.jsp',
                'stepInPageOccurrence': 0,
                'previousStepVariant': {
                    'useCaseName': 'Find Page',
                    'scenarioName': 'find_page_no_result',
                    'stepIndex': 2,
                    'pageName': 'searchResults.jsp',
                    'stepInPageOccurrence': 0,
                    'pageIndex': 1,
                    'pageOccurence': 0
                },
                'previousStep': {
                    'pageName': 'startSearch.jsp',
                    'pageOccurrence': 0,
                    'stepInPageOccurrence': 1
                },
                'nextStep': {
                    'pageName': 'searchResults.jsp',
                    'pageOccurrence': 0,
                    'stepInPageOccurrence': 1
                },
                'pageIndex': 1,
                'pageOccurence': 0,
                'pageOccurenceIndex': 0,
                'pageVariantIndex': 1,
                'pageVariantScenarioIndex': 1,
                'nextStepVariant': {
                    'useCaseName': 'Find Page',
                    'scenarioName': 'find_page_with_text_on_page_from_multiple_results',
                    'stepIndex': 3,
                    'pageName': 'searchResults.jsp',
                    'stepInPageOccurrence': 1,
                    'pageIndex': 1,
                    'pageOccurence': 0
                },
                'pageVariantsCount': 3,
                'pageVariantScenariosCount': 2,
                'previousStepVariantInOtherScenario': {
                    'useCaseName': 'Find Page',
                    'scenarioName': 'find_page_no_result',
                    'stepIndex': 2,
                    'pageName': 'searchResults.jsp',
                    'stepInPageOccurrence': 0,
                    'pageIndex': 1,
                    'pageOccurence': 0
                },
                'nextStepVariantInOtherScenario': null,
                'previousPage': {
                    'pageName': 'startSearch.jsp',
                    'pageOccurrence': 0,
                    'stepInPageOccurrence': 1
                },
                'nextPage': {
                    'pageName': 'contentPage.jsp',
                    'pageOccurrence': 0,
                    'stepInPageOccurrence': 0
                }
            }
        },

        OBJECT_DESCRIPTIONS: [
            {'x': {}}
        ],

        VERSION: {
            version: '1.2.3',
            buildDate: '1/7/2015, 05:00'
        }
    };
});