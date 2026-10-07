# p4sa-plugin

## Introduction

A plugin for Perforce Static Analysis tools (Klocwork and Perforce QAC). Helps automate the scans and retrieval of issue 
metrics for quality gating. Note this plugin does not deploy the static analysis tooling, but simply orchestrates it.

## Prerequisites

- Klocwork or QAC installed
- Active Perforce static analysis license
- Perforce Validate server

## Jenkins Requirements

- Jenkins 2.479 or later

## Installing this plugin

1. Select **Manage Jenkins**
2. Select **Manage Plugins**
3. Select **Available** tab
4. Search for **P4SA**
5. Tick the **P4SA Plugin**
6. Click **Download now and install**
    1. This should download all required dependencies
    2. It may prompt to restart the Jenkins server to take effect after installation

## Global Configuration

No global configuration is required. The analysis tools must be installed on the agent running the job and available in its `PATH`. There are many ways to achieve this depending on the type of job and deployment.  

## Freestyle Analysis Step

Under the **Build Steps** section of the job configuration page select and add the step **Run Perforce Static Analysis**

Configure the input boxes for the step:

- Validate Project URL: The URL for the validate server and project or stream i.e `http://localhost:8080/MyProject` or `http://localhost:8080/MyProject/Stream`
- Select Analysis Engine: Select which Perforce static analysis engine you have installed and wish to run the scan with. The options are QAC or Klocwork.
- Select Analysis Type: Select which type of analysis you wish to run. The two options are:
    - Baseline - Performs a project baseline scan using either `kwbuildproject` (Klocwork) or `qacli validate build` (QAC). This scan will detect all the issues within the project and upload the full list to the validate portal.
    - Delta - Performs a project scan which can be optionally restricted to files in a source file list for Klocwork. This only reports the potential new issues compared with the last baseline scan. Uses the `kwciagent` for Klocwork or `qacli validate cibuild` for QAC. The intention of the delta analysis is to be used as part of a quality gate workflow in merge requests. Blocking merge requests if it were to introduce new issues to the protected branch, which the baseline scan is run on.
- Use Build Command: To generate the capture information needed for analysis the tooling can either watch a build or this can be pre-generated and provided to the analysis. Enable this to provide the build command to capture the data needed . Provide the build command to wrap and capture the compilation data from, i.e `make`. Note in cases where environments or configuration needs to be run before the build command this field is a multi-line field. Only the last command will be captured and thus should be the build command. For example: 
  ```
  ./configure 
  make
  ```
  Would run `./configure` followed by `make`, the capture of compilations would be on the make process.
- Use Pre-generated Build Capture: To generate the capture information needed for analysis the tooling can either watch a build or this can be pre-generated and provided to the analysis. Enable this to provide the location of the file containing the pre-generated capture information. Note, if 'Using Build Command' is also enabled it will ignore this option and use the build command. The path should be the path to the pre-generated capture file in the workspace/agent.
- Extra Options
    - Scan Build Name: When uploading a scan to the validate portal it will be named, these must be unique for each run as it will not overwrite scans of the same name. The default Jenkins variable of `$BUILD_TAG` has been provided which resolves to: `jenkins-${JOB_NAME}-${BUILD_NUMBER}`.
    - Analysis Working Set Filename: If performing a delta analysis with the Klocwork engine you may provide the path to a text file that contains a list of files present in the build specification to restrict the analysis to. An example command to generate this might be: `git diff --name-only origin/${BRANCH} > restriction_list.txt` The required format for the contents being, restriction_list.txt: 
        ```
        sourceFileA.cpp
        sourceFileB.cpp
        sourceFileC.c
        ...
        ```
        Note that any files not present in the build specification can be included, however they will be ignored by the analysis.
    - Enable Quality Gate: For Validate 2026.1 onwards use the inbuilt quality gate feature: [Quality Gate Documentation](https://help.klocwork.com/current/en-us/concepts/ciqualitygate.htm). For older versions enabling the quality gate will set the Job run to Unstable, Failed or allow it to pass depending on whether there are any new issues using the command line output. Note this only applies to the Delta analysis type


## Pipeline Analysis Step

The plugin can also be orchestrated via pipeline and it is recommended to reference the pipeline syntax page for the version of the plugin installed on your server. However a general example of a basic analysis would be:

```
p4StaticAnalysis([
    analysisType: 'Baseline',
    buildCaptureFile: '',
    buildCmd: 'make',
    enableQualityGate: false,
    engine: 'Klocwork',
    restrictionFileList: '',
    scanBuildName: '$BUILD_TAG',
    usingBuildCaptureFile: false,
    usingBuildCmd: true,
    validateProjectURL: 'http://localhost:8080/MyProject'
])
```

## LICENSE

See [LICENSE](LICENSE.md)

