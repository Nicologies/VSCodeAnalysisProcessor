package com.nicologies.VsCodeAnalysisProcessor;

import com.nicologies.VsCodeAnalysisProcessor.common.VsCodeAnalysisConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class VsCodeAnalysisProcessorRunType extends jetbrains.buildServer.serverSide.RunType{
    public VsCodeAnalysisProcessorRunType(final RunTypeRegistry runTypeRegistry, final PluginDescriptor pluginDescriptor){
        runTypeRegistry.registerRunType(this);
    }
    @NotNull
    @Override
    public String getType() {
        return VsCodeAnalysisConstants.RunnerType;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "VisualStudio Code Analysis Result Processor";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "A plugin to get branch name when building Github pull request";
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return null;
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return null;
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return null;
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return null;
    }
}
