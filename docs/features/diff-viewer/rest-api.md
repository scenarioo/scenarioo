#Diff Viewer REST API
## Parameter Overview
| Parameter            | Description      | 
| :------------------- | :------------------------------------    |
| comparisonName       | Name of the active comparison       | 
| baseBranchName       | Name of the active branch      |
| baseBuildName        | Name of the active build      |
| useCaseName          | Name of the active use case      |
| scenarioName         | Name of the active scenairo      |
| stepIndex            | Index of the active step      |

Further information about these parameters can be found in the glossary:
https://github.com/magitnu/scenarioo/blob/develop/documentation/diff-viewer/glossary.md

## API Calls
### BuildDiffInfos
Returns an array of BuildDiffInfo elements

Request URL:
>/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/buildDiffInfos

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/Development/baseBuildName/2014-05-19/buildDiffInfos

Example Response:
```json
[  
   {  
      "comparisonBranchName":"wikipedia-docu-example-dev",
      "comparisonBuildName":"2014-04-19",
      "added":1,
      "changed":2,
      "removed":1,
      "addedElements":[  
         "SwitchLanguageUserInterfaceTest"
      ],
      "removedElements":[  
         {  
            "labels":{  
               "labels":[  

               ],
               "empty":true
            },
            "details":{  
               "Webtest Class":"org.scenarioo.uitest.example.testcases.SwitchLanguageUITest"
            },
            "name":"Switch Language",
            "description":"Search in a different language and switch language of current article.",
            "status":null
         }
      ],
      "name":"To last Sprint",
      "changeRate":73.33333333333334
   }
]
```

### BuildDiffInfo
Returns a BuildDiffInfo element

Request URL:
>/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/buildDiffInfo

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/Development/baseBuildName/2014-05-19/comparisonName/To%20last%20Sprint/buildDiffInfo

Example Response:
```json
{
   "comparisonBranchName":"wikipedia-docu-example-dev",
   "comparisonBuildName":"2014-04-19",
   "added":1,
   "changed":2,
   "removed":1,
   "addedElements":[
      "SwitchLanguageUserInterfaceTest"
   ],
   "removedElements":[
      {
         "labels":{
            "labels":[

            ],
            "empty":true
         },
         "details":{
            "Webtest Class":"org.scenarioo.uitest.example.testcases.SwitchLanguageUITest"
         },
         "name":"Switch Language",
         "description":"Search in a different language and switch language of current article.",
         "status":null
      }
   ],
   "name":"To last Sprint",
   "changeRate":73.33333333333334
}
```

### UseCaseDiffInfo
Returns a UseCaseDiffInfo element

Request URL:
>diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/useCaseDiffInfo

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/Development/baseBuildName/2014-05-19/comparisonName/To%20last%20Sprint/useCaseName/Donate/useCaseDiffInfo

Example Response:
```json
{
   "added":1,
   "changed":0,
   "removed":1,
   "addedElements":[
      "find_donate_page"
   ],
   "removedElements":[
      {
         "numberOfSteps":6,
         "scenario":{
            "labels":{
               "labels":[

               ],
               "empty":true
            },
            "details":{
               "User Role":"unauthenticated",
               "Very Long Metadata Lines":{
                  "Long value with spaces":"Whenever you find yourself on the side of the majority, it is time to pause and reflect. -- Mark Twain",
                  "Long value without spaces":"A_man_who_carries_a_cat_by_the_tail_learns_something_he_can_learn_in_no_other_way._--_Mark_Twain",
                  "A_very_long_metadata_label_without_any_spaces_just_to_test_whether_this_is_displayed_nicely.":"Cheers!"
               }
            },
            "name":"find_donate_page_test",
            "description":"User tries to find out how to donate to Wikipedia.",
            "status":"success"
         }
      }
   ],
   "name":"Donate",
   "changeRate":100.0
}
```

### UseCaseDiffInfos
Returns an array of UseCaseDiffInfo elements

Request URL:
>diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseDiffInfos

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/Development/baseBuildName/2014-05-19/comparisonName/To%20last%20Sprint/useCaseDiffInfos

Example Response:
```json
{  
   "Technical Corner Cases":{  
      "added":2,
      "changed":0,
      "removed":2,
      "addedElements":[  
         "dummy_scenario_with_no_page_names_set_test",
         "dummy_scenario_with_no_steps_test"
      ],
      "removedElements":[  
         {  
            "numberOfSteps":5,
            "scenario":{  
               "labels":{  
                  "labels":[  
                     "rare"
                  ],
                  "empty":false
               },
               "details":{  
                  "User Role":"unauthenticated",
                  "Very Long Metadata Lines":{  
                     "Long value with spaces":"Whenever you find yourself on the side of the majority, it is time to pause and reflect. -- Mark Twain",
                     "Long value without spaces":"A_man_who_carries_a_cat_by_the_tail_learns_something_he_can_learn_in_no_other_way._--_Mark_Twain",
                     "A_very_long_metadata_label_without_any_spaces_just_to_test_whether_this_is_displayed_nicely.":"Cheers!"
                  }
               },
               "name":"dummy_scenario_with_no_page_names_set",
               "description":"Dummy scenario with no page names set for all pages, which should be presented in Scenarioo as if the steps are all for different (unknown) pages.",
               "status":"success"
            }
         },
         {  
            "numberOfSteps":0,
            "scenario":{  
               "labels":{  
                  "labels":[  

                  ],
                  "empty":true
               },
               "details":{  
                  "User Role":"unauthenticated",
                  "Very Long Metadata Lines":{  
                     "Long value with spaces":"Whenever you find yourself on the side of the majority, it is time to pause and reflect. -- Mark Twain",
                     "Long value without spaces":"A_man_who_carries_a_cat_by_the_tail_learns_something_he_can_learn_in_no_other_way._--_Mark_Twain",
                     "A_very_long_metadata_label_without_any_spaces_just_to_test_whether_this_is_displayed_nicely.":"Cheers!"
                  }
               },
               "name":"dummy_scenario_with_no_steps",
               "description":"Dummy scenario with no steps at all.",
               "status":"success"
            }
         }
      ],
      "name":"Technical Corner Cases",
      "changeRate":66.66666666666667
   }
}
```

### ScenarioDiffInfo
Returns a ScenarioDiffInfo element

Request URL:
>diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/scenarioDiffInfo

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/Development/baseBuildName/2014-05-19/comparisonName/To%20Projectstart/useCaseName/Donate/scenarioName/find_donate_page/scenarioDiffInfo

Example Response:
```json
{  
   "added":3,
   "changed":0,
   "removed":4,
   "addedElements":[  
      1,
      2,
      5
   ],
   "removedElements":[  
      {  
         "stepLink":{  
            "pageIndex":1,
            "useCaseName":"Donate",
            "stepIdentifierForObjectRepository":"portal.jsp/0/0",
            "pageName":"portal.jsp",
            "pageOccurrence":0,
            "stepInPageOccurrence":0,
            "stepIndex":1,
            "scenarioName":"find_donate_page"
         },
         "stepDescription":{  
            "screenshotFileName":"001.png",
            "labels":{  
               "labels":[  

               ],
               "empty":true
            },
            "details":{  

            },
            "index":1,
            "status":"success",
            "title":""
         }
      },
      { ... },
      { ... },
      { ... }
   ],
   "name":"find_donate_page",
   "changeRate":70.0
}
```

### ScenarioDiffInfos
Returns an array of ScenarioDiffInfo elements

Request URL:
>/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioDiffInfos

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/wikipedia-docu-example/baseBuildName/2014-03-19/comparisonName/To%20Projectstart/useCaseName/Donate/scenarioDiffInfos

Example Response:
```json
{  
   "find_donate_page":{  
      "added":3,
      "changed":3,
      "removed":4,
      "addedElements":[  
         1,
         2,
         5
      ],
      "removedElements":[  
         {  
            "stepLink":{  
               "pageIndex":1,
               "useCaseName":"Donate",
               "stepIdentifierForObjectRepository":"portal.jsp/0/0",
               "pageName":"portal.jsp",
               "pageOccurrence":0,
               "stepInPageOccurrence":0,
               "stepIndex":1,
               "scenarioName":"find_donate_page"
            },
            "stepDescription":{  
               "screenshotFileName":"001.png",
               "labels":{  
                  "labels":[  

                  ],
                  "empty":true
               },
               "details":{  

               },
               "index":1,
               "status":"success",
               "title":""
            }
         },
         { ... },
         { ... },
         { ... }
      ],
      "name":"find_donate_page",
      "changeRate":74.14100838300001
   }
}
```



### StepDiffInfo
Returns a StepDiffInfo element

Request URL:
>diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/stepIndex/:stepIndex/stepDiffInfo

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/gh-pages/baseBuildName/2016-05-14T10:28:19.082/comparisonName/exampleComparison/useCaseName/OrderPizza/scenarioName/orderPizza_plusRedWine/stepIndex/5/stepDiffInfo

Example Response:
```json
{
   "comparisonScreenshotName":"006.png",
   "pageName":"summary",
   "pageOccurrence":0,
   "stepInPageOccurrence":0,
   "index":5,
   "changeRate":1.25541664
}
```

### StepDiffInfos
Returns an array of StepDiffInfos elements

Request URL:
>/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/stepDiffInfos

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/gh-pages/baseBuildName/2016-05-14T10:28:19.082/comparisonName/exampleComparison/useCaseName/OrderPizza/scenarioName/orderPizza_plusRedWine/stepDiffInfos

Example Response:
```json
{
   "0":{
      "pageName":"enterPhoneNumber",
      "pageOccurrence":0,
      "stepInPageOccurrence":0,
      "comparisonScreenshotName":"000.png",
      "index":0,
      "changeRate":0.0
   },
   "1":{
      "pageName":"confirmAddress",
      "pageOccurrence":0,
      "stepInPageOccurrence":0,
      "comparisonScreenshotName":"001.png",
      "index":1,
      "changeRate":0.0
   },
   "2":{
      "pageName":"selectPizza",
      "pageOccurrence":0,
      "stepInPageOccurrence":0,
      "comparisonScreenshotName":"002.png",
      "index":2,
      "changeRate":0.0
   },
   "3":{
      "pageName":"summary",
      "pageOccurrence":0,
      "stepInPageOccurrence":0,
      "comparisonScreenshotName":"003.png",
      "index":3,
      "changeRate":1.25541664
   }
}
```

### DiffScreenshot
Returns a screenshot with highlighted changes

Request URL:
>/diffViewer/baseBranchName/:baseBranchName/baseBuildName/:baseBuildName/comparisonName/:comparisonName/useCaseName/:useCaseName/scenarioName/:scenarioName/stepIndex/:stepIndex/stepDiffScreenshot

Request Method:
>GET

Example Request:
>http://localhost:8080/scenarioo/rest/diffViewer/baseBranchName/gh-pages/baseBuildName/last%20successful/comparisonName/exampleComparison/useCaseName/OrderPizza/scenarioName/orderPizza_plusRedWine/stepIndex/5/stepDiffScreenshot

Example Response:
![diffScreenshot](http://s33.postimg.org/duhfqsw8v/step_Diff_Screenshot.png)
