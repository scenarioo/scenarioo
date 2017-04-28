angular.module('scenarioo.controllers').controller('DataController', DataController);


function DataController(BranchesAndBuildsService, UseCasesResource,ScenarioResource){

    var dat = this;

    BranchesAndBuildsService.getBranchesAndBuilds()
        .then(function onSuccess(branchesAndBuilds) {
            dat.viewdata = branchesAndBuilds;

            for(var branchid = 0; branchid < dat.viewdata.branches.length; branchid++){
                for(var buildid = 0; buildid < dat.viewdata.branches[branchid].builds.length; buildid++){
                    const currentBuildId = buildid;
                    const currentBranchid = branchid;
                    const branchName = dat.viewdata.branches[branchid].branch.name;
                    const buildName = dat.viewdata.branches[branchid].builds[buildid].build.name;

                    UseCasesResource.query(
                        {'branchName': branchName, 'buildName': buildName},
                        function onSuccess(useCases) {
                            console.log(useCases);
                            dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases = useCases;

                            for(var usecaseid = 0; usecaseid < dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases.length; usecaseid++){
                                const currentUseCaseId = usecaseid;
                                const useCaseName = dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases[currentUseCaseId].name;
                                ScenarioResource.get({
                                    branchName: branchName,
                                    buildName: buildName,
                                    usecaseName: useCaseName
                                }, function (result) {
                                    dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases[currentUseCaseId].scenarios = result.scenarios;

                                    for(var scenarioid = 0; scenarioid < dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases[currentUseCaseId].scenarios.length; scenarioid++){
                                        const currentScenarioId = scenarioid;
                                        const scenarioName = dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases[currentUseCaseId].scenarios[currentScenarioId].scenario.name;

                                        ScenarioResource.get({
                                            branchName: branchName,
                                            buildName: buildName,
                                            usecaseName: useCaseName,
                                            scenarioName: scenarioName
                                        }, function (result) {
                                            console.log(result);
                                            dat.viewdata.branches[currentBranchid].builds[currentBuildId].useCases[currentUseCaseId].scenarios[currentScenarioId].steps = result;

                                        });

                                    }

                                });
                            }

                        });
                    break;
                }
                break;
            }

        });


}
