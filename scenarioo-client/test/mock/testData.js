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
                    {'details': {'url': 'http://www.wikipedia.org'}, 'status': 'success', 'occurence': 0, 'relativeIndex': 0, 'title': 'Wikipedia Suche', 'screenshotFileName': '000.png', 'variantIndex': 3, 'previousStepVariant': {'occurence': 0, 'relativeIndex': 1, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_no_result', 'pageName': 'startSearch.jsp', 'index': 1}, 'nextStepVariant': {'occurence': 0, 'relativeIndex': 1, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_text_on_page_from_multiple_results', 'pageName': 'startSearch.jsp', 'index': 1}, 'index': 0},
                    {'details': {'url': 'http://www.wikipedia.org'}, 'status': 'success', 'occurence': 0, 'relativeIndex': 1, 'title': 'Wikipedia Suche', 'screenshotFileName': '001.png', 'variantIndex': 4, 'previousStepVariant': {'occurence': 0, 'relativeIndex': 0, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_text_on_page_from_multiple_results', 'pageName': 'startSearch.jsp', 'index': 0}, 'nextStepVariant': {'occurence': 0, 'relativeIndex': 0, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_title_ambiguous_navigate_to_other_meaning', 'pageName': 'startSearch.jsp', 'index': 0}, 'index': 1}
                ]},
                {'page': {'details': {}, 'name': 'searchResults.jsp'}, 'steps': [
                    {'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'}, 'status': 'success', 'occurence': 1, 'relativeIndex': 0, 'title': 'Search results', 'screenshotFileName': '002.png', 'variantIndex': 2, 'previousStepVariant': {'occurence': 1, 'relativeIndex': 0, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_no_result', 'pageName': 'searchResults.jsp', 'index': 2}, 'nextStepVariant': {'occurence': 1, 'relativeIndex': 1, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_text_on_page_from_multiple_results', 'pageName': 'searchResults.jsp', 'index': 3}, 'index': 2},
                    {'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'}, 'status': 'success', 'occurence': 1, 'relativeIndex': 1, 'title': 'Search results', 'screenshotFileName': '003.png', 'variantIndex': 3, 'previousStepVariant': {'occurence': 1, 'relativeIndex': 0, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_text_on_page_from_multiple_results', 'pageName': 'searchResults.jsp', 'index': 2}, 'nextStepVariant': {'occurence': 1, 'relativeIndex': 2, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_text_on_page_from_multiple_results', 'pageName': 'searchResults.jsp', 'index': 4}, 'index': 3},
                    {'details': {'url': 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'}, 'status': 'success', 'occurence': 1, 'relativeIndex': 2, 'title': 'Search results', 'screenshotFileName': '004.png', 'variantIndex': 4, 'previousStepVariant': {'occurence': 1, 'relativeIndex': 1, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_with_text_on_page_from_multiple_results', 'pageName': 'searchResults.jsp', 'index': 3}, 'nextStepVariant': {'occurence': 1, 'relativeIndex': 0, 'useCaseName': 'Find Page', 'scenarioName': 'find_page_no_result', 'pageName': 'searchResults.jsp', 'index': 2}, 'index': 4}
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

        STEP: {
            'html': {
                'htmlSource': '<html>\n<head>\n</head>\n<body>\n   <p>just some dummy html code</p>\n</body>\n</html>'
            },
            'page': {
                'details': {
                },
                'name': 'startSearch.jsp'
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
                        'details': {
                        },
                        'item': {
                            'details': {
                            },
                            'name': 'http://www.wikipedia.org',
                            'type': 'httpCall'
                        }
                    }
                },
                'visibleText': 'Bla bla bla bla bla ... This is the visible text as generated from dummy test.'
            },
            'stepDescription': {
                'details': {
                    'url': 'http://www.wikipedia.org'
                },
                'title': 'Search results',
                'screenshotFileName': '000.png',
                'variantIndex': 0,
                'previousStepVariant': null,
                'nextStepVariant': null,
                'status': 'success',
                'occurence': 0,
                'relativeIndex': 0,
                'index': 0
            }
        },

        PAGE_VARIANTS: {
            'counters': {
                'startSearch.jsp': 8,
                'searchResults.jsp': 4,
                'contentPage.jsp': 6
            }
        }
    };
});