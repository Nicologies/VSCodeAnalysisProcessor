package com.nicologies.VsCodeAnalysisProcessor;

import com.nicologies.VsCodeAnalysisProcessor.common.VsCodeAnalysisConstants;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.inspections.InspectionReporter;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class BuildServiceFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo {
    private static final Logger LOG = Logger.getLogger(BuildServiceFactory.class);
    private InspectionReporter _inspectionsReporter;

    public BuildServiceFactory(@NotNull final InspectionReporter inspectionsReporter) {
        _inspectionsReporter = inspectionsReporter;
    }

    @NotNull
    public String getType() {
        return VsCodeAnalysisConstants.RunnerType;
    }

    public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
        return true;
    }


    @NotNull
    public CommandLineBuildService createService() {
        return new BuildService(_inspectionsReporter);
    }

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return this;
    }
}