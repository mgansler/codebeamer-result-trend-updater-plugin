/*
 * Copyright (c) 2015 Intland Software (support@intland.com)
 */

package com.intland.jenkins.util;

import com.intland.jenkins.collector.dto.BuildDto;
import com.intland.jenkins.collector.dto.PerformanceDto;
import com.intland.jenkins.collector.dto.ScmDto;
import com.intland.jenkins.collector.dto.TestResultDto;

public class WikiMarkupBuilder {
    private static final String SUCCESS_TITLE_COLOR = "#000000";
    private static final String FAIL_TITLE_COLOR = "#ff0000";

    private final String TEMPLATE_HEADER = "\n//DO NOT MODIFY! \n//Generated by plugin version: $pluginVersion$ at: $buildTime$" +
            "\n!2 %%(color: $titleColor$;)Build #$buildId$ ($buildTime$)%!\n[{Table\n\n" +
            "|__Duration__\n" +
            "|[$duration$|$projectUrl$buildTimeTrend]$testDuration$ @ $builtOn$\n\n| \n";
    private final String TEMPLATE_TESTREPORT = "__Test Result__ \n" +
            "|__[$failedTestCount$/$allTestCount$|$buildUrl$testReport/] failures__ $failedDifference$\n\n";
    private final String TEMPLATE_PERFORMANCE = "__Performance Result__ \n" +
            "|__\n" +
            "*Average response time: $averageResponseTime$ ms \n" +
            "*Median response time: $medianResponseTime$ ms \n" +
            "*Maximum response time: $maximumResponseTime$ ms \n\n";
    private final String TEMPLATE_FOOTER = "|__[Tested changes|$buildUrl$changes]__\n" +
            "|__\n" +
            "$changes$ \n\n" +
            "|__Repository__\n" +
            "|$repositoryLine$\n" +
            "}] \n";

    private String content;

    public WikiMarkupBuilder() {
    }

    public WikiMarkupBuilder initWithTestReportTemplate() {
        content = TEMPLATE_HEADER + TEMPLATE_TESTREPORT + TEMPLATE_FOOTER;
        return this;
    }

    public WikiMarkupBuilder initWithPerformanceTemplate() {
        content = TEMPLATE_HEADER + TEMPLATE_PERFORMANCE + TEMPLATE_FOOTER;
        return this;
    }

    public WikiMarkupBuilder withBuildInfo(BuildDto buildDto) {
        return this.withBuildId(buildDto.getBuildNumber())
                    .withPluginVersion(buildDto.getPluginVersion())
                    .withDuration(buildDto.getFormattedBuildDuration())
                    .withProjectUrl(buildDto.getProjectUrl())
                    .withBuildTime(buildDto.getFormattedBuildTime())
                    .withBuildUrl(buildDto.getBuildUrl())
                    .withBuiltOn(buildDto.getBuiltOn());
    }

    public WikiMarkupBuilder withTestReportInfo(TestResultDto testResultDto) {
        String titleColor = testResultDto.getFailCount() == 0 && testResultDto.getTotalCount() > 0
                            ? SUCCESS_TITLE_COLOR : FAIL_TITLE_COLOR;
        return this.withTitleColor(titleColor)
                .withTestDuration(testResultDto.getFormattedTestDuration())
                .withFailedDifference(testResultDto.getFailedDifference())
                .withAllTestCount(String.valueOf(testResultDto.getTotalCount()))
                .withFailedTestCount(String.valueOf(testResultDto.getFailCount()));
    }

    public WikiMarkupBuilder withPerformanceInfo(PerformanceDto performanceDto) {
        String titleColor = !performanceDto.isFailed() ? SUCCESS_TITLE_COLOR : FAIL_TITLE_COLOR;
        return this.withAverageResponseTime(String.valueOf(performanceDto.getAverageResponseTime()))
                .withMedianResponseTime(String.valueOf(performanceDto.getMedianResponseTime()))
                .withMaximumResponseTime(String.valueOf(performanceDto.getMaximumResponseTime()))
                .withTestDuration("")
                .withTitleColor(titleColor);

    }

    public WikiMarkupBuilder withScmInfo(ScmDto scmDto) {
        return this.withRepositoryLine(scmDto.getRepositoryLine())
                    .withChanges(scmDto.getChanges());
    }

    private WikiMarkupBuilder withBuildId(String buildId) {
        content = content.replace("$buildId$", buildId);
        return this;
    }

    private WikiMarkupBuilder withPluginVersion(String pluginVersion) {
        content = content.replace("$pluginVersion$", pluginVersion);
        return this;
    }

    private WikiMarkupBuilder withDuration(String duration) {
        content = content.replace("$duration$", duration);
        return this;
    }

    private WikiMarkupBuilder withProjectUrl(String projectUrl) {
        content = content.replace("$projectUrl$", projectUrl);
        return this;
    }

    private WikiMarkupBuilder withBuildTime(String buildTime) {
        content = content.replaceAll("\\$buildTime\\$", buildTime);
        return this;
    }

    private WikiMarkupBuilder withBuildUrl(String buildUrl) {
        content = content.replaceAll("\\$buildUrl\\$", buildUrl);
        return this;
    }

    private WikiMarkupBuilder withBuiltOn(String builtOn) {
        content = content.replace("$builtOn$", builtOn);
        return this;
    }

    public WikiMarkupBuilder withTitleColor(String titleColor) {
        content = content.replace("$titleColor$", titleColor);
        return this;
    }

    private WikiMarkupBuilder withTestDuration(String testDuration) {
        String duration = !testDuration.equals("") ? ", test duration: " + testDuration : "";
        content = content.replace("$testDuration$", duration);
        return this;
    }

    private WikiMarkupBuilder withFailedDifference(String failedDifference) {
        content = content.replace("$failedDifference$", failedDifference);
        return this;
    }

    private WikiMarkupBuilder withAllTestCount(String allTestCount) {
        content = content.replace("$allTestCount$", allTestCount);
        return this;
    }

    private WikiMarkupBuilder withFailedTestCount(String failedTestCount) {
        content = content.replaceAll("\\$failedTestCount\\$", failedTestCount);
        return this;
    }

    private WikiMarkupBuilder withAverageResponseTime(String averageResponseTime) {
        content = content.replace("$averageResponseTime$", averageResponseTime);
        return this;
    }

    private WikiMarkupBuilder withMedianResponseTime(String medianResponseTime) {
        content = content.replace("$medianResponseTime$", medianResponseTime);
        return this;
    }

    private WikiMarkupBuilder withMaximumResponseTime(String maximumResponseTime) {
        content = content.replace("$maximumResponseTime$", maximumResponseTime);
        return this;
    }

    private WikiMarkupBuilder withChanges(String changes) {
        content = content.replace("$changes$", changes);
        return this;
    }

    private WikiMarkupBuilder withRepositoryLine(String repositoryLine) {
        content = content.replace("$repositoryLine$", repositoryLine);
        return this;
    }

    public String build() {
        return content;
    }
}
