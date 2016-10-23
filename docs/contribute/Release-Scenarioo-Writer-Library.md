## Release Java library

See [scenarioo-java](https://github.com/scenarioo/scenarioo-java/wiki/Release-new-API-version) repo.

## Release C&#35; library

The pre-condition is, that you pulled the whole scenarioo-cs repository. Set the final version number in `AddemblyInfo.cs` or in the Scenarioo project properties equal to x.y.z. The build number will automatically set in build server. Your final `git push` will trigger the Continuous Integration build on AppVeyor and deploy all build artifacts to NuGet store where you can shortly get it from there. To verify the build status, please refer to https://github.com/scenarioo/scenarioo-cs.

The NuGet package is reachable by: https://www.nuget.org/packages/Scenarioo-cs/