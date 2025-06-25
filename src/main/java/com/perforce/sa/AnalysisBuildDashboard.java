package com.perforce.sa;

import hudson.EnvVars;
import hudson.model.Action;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AnalysisBuildDashboard implements Action {

    private String validatePortalURL;
    private String displayName;

    public AnalysisBuildDashboard(EnvVars env, AnalysisBuilderConfig analysisConfig, String url) {
        String projectUrlName = "";
        if (analysisConfig.getValidateProjectId() == null) {
            projectUrlName = analysisConfig.getValidateProjectName();
        } else {
            projectUrlName = analysisConfig.getValidateProjectId();
        }

        if (url != null && !url.trim().isEmpty()) {
            this.validatePortalURL = url;
        } else {
            try {
                this.validatePortalURL = UtilityFunctions.getValidateServerURL(analysisConfig.getValidateProjectURL())
                        + "/review/insight-review.html#issuelist_goto:project="
                        + projectUrlName
                        + ",searchquery=build%253A'"
                        + URLEncoder.encode(
                                UtilityFunctions.resolveEnvVarsInConfig(env, analysisConfig.getScanBuildName()),
                                StandardCharsets.UTF_8.toString())
                        + "'";
            } catch (UnsupportedEncodingException e) {
                this.validatePortalURL = UtilityFunctions.getValidateServerURL(analysisConfig.getValidateProjectURL())
                        + "/review/insight-review.html#issuelist_goto:project="
                        + projectUrlName;
            }
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
