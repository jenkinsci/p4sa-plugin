package com.perforce.sa;

import hudson.EnvVars;
import hudson.model.Action;

public class AnalysisBuildDashboard implements Action {

    private String validatePortalURL;
    private String displayName;

    public AnalysisBuildDashboard(EnvVars env, AnalysisBuilderConfig analysisConfig) {
        String projectUrlName = "";
        if (analysisConfig.getValidateProjectId() == null) {
            projectUrlName = analysisConfig.getValidateProjectName();
        } else {
            projectUrlName = analysisConfig.getValidateProjectId();
        }
        if (analysisConfig.getAnalysisType().equals("Baseline")) {
            this.validatePortalURL = UtilityFunctions.getValidateServerURL(analysisConfig.getValidateProjectURL())
                    + "/review/insight-review.html#issuelist_goto:project="
                    + projectUrlName
                    + ",searchquery=build%253A'"
                    + UtilityFunctions.resolveEnvVarsInConfig(env, analysisConfig.getScanBuildName())
                    + "'";
        } else {
            this.validatePortalURL = UtilityFunctions.getValidateServerURL(analysisConfig.getValidateProjectURL())
                    + "/review/insight-review.html#issuelist_goto:project="
                    + projectUrlName
                    + ",searchquery=ci%253A'"
                    + UtilityFunctions.resolveEnvVarsInConfig(env, analysisConfig.getScanBuildName())
                    + "'";
        }
        this.displayName = analysisConfig.getEngine() + " Scan Results";
    }

    @Override
    public String getUrlName() {
        return this.validatePortalURL;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getIconFileName() {
        return "symbol-logo-perforce-icon-reg plugin-p4sa";
    }
}
