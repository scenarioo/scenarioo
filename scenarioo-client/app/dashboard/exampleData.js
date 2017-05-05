var date = new Date();
var date2 = new Date().setFullYear(date.getFullYear(), date.getMonth()+3, date.getDay());

var LINKS = [
    {
        name: 'Some Link name to be displayed',
        url:'http://github.com/scenarioo/scenarioo'
    },
    {
        name: 'Some other Link name to be displayed',
        url:'http://github.com/scenarioo/scenarioo'
    },
];

var File_MD_1 = {
    name: 'Documentation FILE 1',
    url: 'https://raw.githubusercontent.com/scenarioo/scenarioo/develop/README.md',
    content: '',//if no content given, file will be loaded from url
    type: 'md',
    links: LINKS,
};

var File_MD_2 = {
    name: 'Doc 2',
    url: 'https://raw.githubusercontent.com/showdownjs/showdown/master/README.md',
    content: '',//if no content given, file will be loaded from url
    type: 'md',
    links: [],
};

var File_Java_1 = {
    name: 'Test Sources Java',
    url: 'https://raw.githubusercontent.com/scenarioo/scenarioo/develop/scenarioo-server/src/test/java/org/scenarioo/rest/base/StepIdentifierTest.java',
    content: '',//if no content given, file will be loaded from url
    type: 'java',
    links: LINKS,
};

var File_JS_1 = {
    name: 'JavaScript Tests',
    url: 'https://raw.githubusercontent.com/scenarioo/scenarioo-js/develop/test/unit/docuWriter/identifierSanitizerTest.js',
    content: '',//if no content given, file will be loaded from url
    type: 'js',
    links: [],
};


var MARKDOWNS = [File_MD_1, File_MD_2];

var TESTSOURCES = [File_JS_1, File_Java_1];

var TestResult1 = {
    name: 'UnitTest1',
    status: 'failed',
    output: 'some Error Log'
};

var TestResult2 = {
    name: 'UnitTest2',
    status: 'success',
    output: 'some output'
};


var TESTRESULTS=[TestResult1, TestResult2];


var FEAT_1 = {
    name: 'Feature Name F1', //Unique name in one Build
    description: 'Some Feature Description text',
    orderIndex: 3, //Ordering for dashboard view former 'storyOrderNumber'
    milestone: date2, //Some date of a release
    type: 'SomeType As String p.Ex:Use Case, Feature, ...',
    status: 'unimplemented',// should be calculated by own status and subfeature states
    links: LINKS,
    features: [], //No subfeatures
    markdown: MARKDOWNS,
    testsources: [],
    testresults: []
};


var FEAT_2 = {
    name: 'Feature Name F2', //Unique name in one Build
    description: 'Some Feature Description text',
    orderIndex: 1, //Ordering for dashboard view former 'storyOrderNumber'
    milestone: date, //Some date of a release
    type: 'SomeType As String p.Ex:Use Case, Feature, ...',
    status: 'failed',// should be calculated by own status and subfeature states
    links: LINKS,
    features: [FEAT_1],
    markdown: MARKDOWNS,
    testsources: TESTSOURCES,
    testresults: TESTRESULTS
};


var FEATURES = [

];

var FEATURES = [
    {
        name: 'Maintain product Catalogue',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
        features: [
            {
                name: 'CRUD of Products',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Maintain Tag Hierarchy',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
        ]
    },{
        name: 'Browse Products',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
        features: [
            {
                name: 'View Product Detail',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Navigate by Tags',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Feature Product',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Advertise Specials',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Search Sauce',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
        ]
    },{
        name: 'Shopping Card',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
        features: [
            {
                name: 'Maintain Cart',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Store Cart',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
        ]
    },{
        name: 'Checkout & Pay',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
        features: [
            {
                name: 'Complete Order',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
                features: [
                    {
                        name: 'Register Customer',
                        description: 'Some Feature Description text',
                        orderIndex: 1,
                        milestone: date,
                        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                        status: 'failed',
                        links: LINKS,
                        markdown: MARKDOWNS,
                        testsources: TESTSOURCES,
                        testresults: TESTRESULTS,
                    },{
                        name: 'Recover PW',
                        description: 'Some Feature Description text',
                        orderIndex: 1,
                        milestone: date,
                        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                        status: 'failed',
                        links: LINKS,
                        markdown: MARKDOWNS,
                        testsources: TESTSOURCES,
                        testresults: TESTRESULTS,
                    },{
                        name: 'Delete Customer',
                        description: 'Some Feature Description text',
                        orderIndex: 1,
                        milestone: date,
                        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                        status: 'failed',
                        links: LINKS,
                        markdown: MARKDOWNS,
                        testsources: TESTSOURCES,
                        testresults: TESTRESULTS,
                    },
                    {
                        name: 'Blacklist Customer',
                        description: 'Some Feature Description text',
                        orderIndex: 1,
                        milestone: date,
                        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                        status: 'failed',
                        links: LINKS,
                        markdown: MARKDOWNS,
                        testsources: TESTSOURCES,
                        testresults: TESTRESULTS,
                        features: [
                            {
                                name: 'Register Customer',
                                description: 'Some Feature Description text',
                                orderIndex: 1,
                                milestone: date,
                                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                                status: 'failed',
                                links: LINKS,
                                markdown: MARKDOWNS,
                                testsources: TESTSOURCES,
                                testresults: TESTRESULTS,
                            },{
                                name: 'Recover PW',
                                description: 'Some Feature Description text',
                                orderIndex: 1,
                                milestone: date,
                                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                                status: 'failed',
                                links: LINKS,
                                markdown: MARKDOWNS,
                                testsources: TESTSOURCES,
                                testresults: TESTRESULTS,
                            },{
                                name: 'Delete Customer',
                                description: 'Some Feature Description text',
                                orderIndex: 1,
                                milestone: date,
                                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                                status: 'failed',
                                links: LINKS,
                                markdown: MARKDOWNS,
                                testsources: TESTSOURCES,
                                testresults: TESTRESULTS,
                            },
                            {
                                name: 'Blacklist Customer',
                                description: 'Some Feature Description text',
                                orderIndex: 1,
                                milestone: date,
                                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                                status: 'failed',
                                links: LINKS,
                                markdown: MARKDOWNS,
                                testsources: TESTSOURCES,
                                testresults: TESTRESULTS,
                            },
                        ]
                    },
                ]
            },{
                name: 'Confirm by Email',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },{
                name: 'Order by Email',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Validate with Credit Card',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
        ]
    },{
        name: 'Process Order',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
    },{
        name: 'Stock and Delivery',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
    },{
        name: 'Register Customer',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
        features: [
            {
                name: 'Register Customer',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },{
                name: 'Recover PW',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },{
                name: 'Delete Customer',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
            {
                name: 'Blacklist Customer',
                description: 'Some Feature Description text',
                orderIndex: 1,
                milestone: date,
                type: 'SomeType As String p.Ex:Use Case, Feature, ...',
                status: 'failed',
                links: LINKS,
                markdown: MARKDOWNS,
                testsources: TESTSOURCES,
                testresults: TESTRESULTS,
            },
        ]
    },{
        name: 'Gift Cards',
        description: 'Some Feature Description text',
        orderIndex: 1,
        milestone: date,
        type: 'SomeType As String p.Ex:Use Case, Feature, ...',
        status: 'failed',
        links: LINKS,
        markdown: MARKDOWNS,
        testsources: TESTSOURCES,
        testresults: TESTRESULTS,
    },
];





var feature = { //Currently Selected Feature
    name: 'Feature Name', //Unique name in one Build
    description: 'Some Feature Description text',
    orderIndex: 1, //Ordering for dashboard view former 'storyOrderNumber'
    milestone: date, //Some date of a release
    type: 'SomeType As String p.Ex:Use Case, Feature, ...',
    status: 'failed',// should be calculated by own status and subfeature states
    links: LINKS,
    features: FEATURES,
    markdown: MARKDOWNS,
    testsources: TESTSOURCES,
    testresults: TESTRESULTS
};
