package com.perforce.sa;

import hudson.model.Action;

public class AnalysisProjectDashboard implements Action {

    private String validatePortalURL;

    public AnalysisProjectDashboard(AnalysisBuilderConfig analysisConfig) {
        String projectURLName = "";
        if (analysisConfig.getValidateProjectId() == null) {
            projectURLName = analysisConfig.getValidateProjectName();
        } else {
            projectURLName = analysisConfig.getValidateProjectId();
        }
        this.validatePortalURL = UtilityFunctions.getValidateServerURL(analysisConfig.getValidateProjectURL())
                + "/review/insight-review.html#reportviewer_goto:project="
                + projectURLName;
    }

    @Override
    public String getUrlName() {
        return this.validatePortalURL;
    }

    @Override
    public String getDisplayName() {
        return "Perforce Validate";
    }

    @Override
    public String getIconFileName() {
        return "symbol-logo-perforce-icon-reg plugin-p4sa";
    }
}
