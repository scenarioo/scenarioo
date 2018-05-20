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
            'defaultBuildName': 'current',
            'scenarioPropertiesInOverview': 'userProfile, configuration',
            'applicationInformation': 'This is my personal copy of Scenarioo :-)',
            'buildstates': {
                BUILD_STATE_FAILED: 'label-important',
                BUILD_STATE_SUCCESS: 'label-success',
                BUILD_STATE_WARNING: 'label-warning'
            },
            'defaultBranchName': 'trunk',
            'customObjectTabs': [
                {
                    'id': 'calls',
                    'tabTitle': 'Calls',
                    'objectTypesToDisplay': ['uiAction', 'businessOperation', 'service'],
                    'customObjectDetailColumns': [
                        {
                            'columnTitle': 'Description',
                            'propertyKey': 'description'
                        },
                        {
                            'columnTitle': 'Real Service Name',
                            'propertyKey': 'realName'
                        }
                    ]
                }
            ]
        },

        CONFIG_PAGES_EXPANDED: {
                'defaultBuildName': 'current',
                'scenarioPropertiesInOverview': 'userProfile, configuration',
                'applicationInformation': 'This is my personal copy of Scenarioo :-)',
                'buildstates': {
                    BUILD_STATE_FAILED: 'label-important',
                    BUILD_STATE_SUCCESS: 'label-success',
                    BUILD_STATE_WARNING: 'label-warning'
                },
                'defaultBranchName': 'trunk',
                'expandPagesInScenarioOverview': true,
        },

        BRANCHES: [
            {
                'branch': {
                    'description': 'Just an example development branch from example docu generation example.',
                    'name': 'release-branch-2013-11-14'
                }, 'builds': [
                {
                    'linkName': 'example-build',
                    'build': {
                        'details': {},
                        'status': 'success',
                        'revision': '123456',
                        'date': 1385284268394,
                        'name': 'example-build'
                    }
                }
            ]
            },
            {
                'branch': {
                    'description': 'Just an example development branch from example docu generation example.',
                    'name': 'release-branch-2014-01-16'
                }, 'builds': [
                {
                    'linkName': 'example-build',
                    'build': {
                        'details': {},
                        'status': 'success',
                        'revision': '123456',
                        'date': 1385284268394,
                        'name': 'example-build'
                    }
                }
            ]
            },
            {
                'branch': {
                    'description': 'Just an example development branch from example docu generation example.',
                    'name': 'trunk'
                }, 'builds': [
                {
                    'linkName': 'example-build',
                    'build': {
                        'details': {},
                        'status': 'success',
                        'revision': '123456',
                        'date': 1385284268394,
                        'name': 'example-build'
                    }
                }
            ]
            }
        ],

        BUILD_IMPORT_STATES: [
            {
                'buildDescription': {
                    'details': {},
                    'revision': '123456',
                    'status': 'success',
                    'date': 1388879915785,
                    'name': 'example-build'
                },
                'importDate': 1388620336537,
                'status': 'SUCCESS',
                'identifier': {'branchName': 'wikipedia-docu-example', 'buildName': 'example-build'}
            }
        ],

        USECASES: [
            {
                'useCase': {
                    'details': {'webtestClass': 'org.scenarioo.uitest.example.testcases.FindPageUITest'},
                    'status': 'success',
                    'description': 'User wants to search for a page and read it.',
                    'name': 'Find Page'
                }, 'scenarios': [
                {
                    'details': {'userRole': 'unauthenticated'},
                    'status': 'success',
                    'description': 'User enters text that is not found in pages content.',
                    'name': 'find_page_no_result'
                },
                {
                    'details': {'userRole': 'unauthenticated'},
                    'status': 'success',
                    'description': 'User enters some text and finds multiple pages that contain this text.',
                    'name': 'find_page_with_text_on_page_from_multiple_results'
                },
                {
                    'details': {'userRole': 'unauthenticated'},
                    'status': 'success',
                    'description': 'User enters page title that is ambiguous but matches directly a page, on the page he sees the list of other meanings, and can navigate to the page he meant.',
                    'name': 'find_page_with_title_ambiguous_navigate_to_other_meaning'
                },
                {
                    'details': {'userRole': 'unauthenticated'},
                    'status': 'success',
                    'description': 'User enters exact page title and finds it directly.',
                    'name': 'find_page_with_title_direct'
                }
            ]
            }
        ],

        SCENARIO: {
            'useCase': {
                'details': {
                    'webtestClass': 'org.scenarioo.uitest.example.testcases.FindPageUITest'
                },
                'status': 'success',
                'description': 'User wants to search for a page and read it.',
                'name': 'Find Page',
                'labels': {
                    'empty': true,
                    'label': []
                }
            },
            'pagesAndSteps': [
                {
                    'page': {'details': {}, 'name': 'startSearch.jsp'}, 'steps': [
                    {
                        'details': {'url': 'http://www.wikipedia.org'},
                        'status': 'success',
                        'title': 'Wikipedia Suche',
                        'screenshotFileName': '000.png',
                        'index': 0
                    },
                    {
                        'details': {'url': 'http://www.wikipedia.org'},
                        'status': 'success',
                        'title': 'Wikipedia Suche',
                        'screenshotFileName': '001.png',
                        'index': 1
                    }
                ]
                },
                {
                    'page': {'details': {}, 'name': 'searchResults.jsp'}, 'steps': [
                    {
                        'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'},
                        'status': 'success',
                        'title': 'Search results',
                        'screenshotFileName': '002.png',
                        'index': 2
                    },
                    {
                        'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'},
                        'status': 'success',
                        'title': 'Search results',
                        'screenshotFileName': '003.png',
                        'index': 3
                    },
                    {
                        'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'},
                        'status': 'success',
                        'title': 'Search results',
                        'screenshotFileName': '004.png',
                        'index': 4
                    }
                ]
                }
            ],
            'scenario': {
                'details': {
                    'userRole': 'unauthenticated'
                },
                'status': 'success',
                'description': 'User enters some text and finds multiple pages that contain this text.',
                'name': 'find_page_with_text_on_page_from_multiple_results',
                'labels': {
                    'empty': true,
                    'label': []
                }
            },
            'scenarioStatistics': {
                'numberOfPages': 2,
                'numberOfSteps': 5
            }
        },

        /**
         * Based on
         * http://localhost:8080/scenarioo/rest/branch/wikipedia-docu-example/build/example-build/usecase/Find%20Page/scenario/find_page_with_text_on_page_from_multiple_results/pageName/searchResults.jsp/pageOccurrence/0/stepInPageOccurrence/0
         * with simplified metadata.
         */
        STEP: {
            'stepIdentifier': {
                'branchName': 'wikipedia-docu-example',
                'buildName': '2014-03-19',
                'usecaseName': 'Donate',
                'scenarioName': 'find_donate_page',
                'labels': null,
                'pageName': 'startSearch.jsp',
                'pageOccurrence': 0,
                'stepInPageOccurrence': 0
            },
            'stepStatistics': {
                'totalNumberOfStepsInScenario': 5,
                'totalNumberOfStepsInPageOccurrence': 2,
                'totalNumberOfPagesInScenario': 3
            },
            'useCaseLabels': {
                'label': ['normal-case'],
                'empty': false
            },
            'scenarioLabels': {
                'label': ['no results'],
                'empty': false
            },
            'step': {
                'stepDescription': {
                    'details': {
                        'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'
                    },
                    'status': 'success',
                    'title': 'Search results',
                    'screenshotFileName': '002.png',
                    'index': 2,
                    'labels': {
                        'label': ['step-label-0', 'public'],
                        'empty': false
                    }
                },
                'metadata': {
                    'details': {
                        'simulationConfiguration': {
                            'details': {
                                'overridenConfigModules': [],
                                'defaultConfigModules': []
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
                    'name': 'searchResults.jsp',
                    'labels': {
                        'label': ['page-label1', 'page-label2'],
                        'empty': false
                    }
                },
                'diffInfo':{
                    'changed': 1,
                    'added': 0,
                    'removed': 0,
                    'isAdded': false,
                    'isRemoved': false
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
                    'pageOccurrence': 0
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
                'pageOccurrence': 0,
                'pageOccurrenceIndex': 0,
                'pageVariantIndex': 1,
                'pageVariantScenarioIndex': 1,
                'nextStepVariant': {
                    'useCaseName': 'Find Page',
                    'scenarioName': 'find_page_with_text_on_page_from_multiple_results',
                    'stepIndex': 3,
                    'pageName': 'searchResults.jsp',
                    'stepInPageOccurrence': 1,
                    'pageIndex': 1,
                    'pageOccurrence': 0
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
                    'pageOccurrence': 0
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
            buildDate: '1/7/2015, 05:00',
            apiVersion: '1.2.1',
            aggregatedDataFormatVersion: '1.2.3',
            releaseBranch: 'release-1.2'
        },

        TABS: [
            {
                tabId: 'usecases',
                title: 'Use Cases',
                contentViewUrl: 'build/useCasesTab.html',
                active: true
            }
        ]

    };
});
