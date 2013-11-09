

function createConfigFake() {
    return {
        branch : undefined,
        build : undefined,

        isLoaded : function() {
            return angular.isDefined(this.build) && angular.isDefined(this.build);
        },

        selectedBranch : function() {
            return this.branch;
        },

        selectedBuild : function() {
            return this.build;
        },

        selectedBuildAndBranch : function() {
            return {
                branch: this.selectedBranch(),
                build: this.selectedBuild()
            }
        },

        setSelectedBranch : function(branch) {
            this.branch = branch;
        },

        setSelectedBuild : function(build) {
            this.build = build;
        },

        scenarioPropertiesInOverview : function() {
            return [
                {
                    text: 'User Profile',
                    property: 'details.properties.userProfile',
                    attr: 'userProfile'
                }
            ];
        }
    };
}