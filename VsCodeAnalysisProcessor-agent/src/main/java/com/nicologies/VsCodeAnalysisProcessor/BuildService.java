package com.nicologies.VsCodeAnalysisProcessor;

import com.intellij.openapi.util.SystemInfo;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.inspections.InspectionReporter;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.fxcop.agent.FxCopFileProcessor;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.util.AntPatternFileFinder;
import jetbrains.buildServer.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BuildService extends BuildServiceAdapter {
    private InspectionReporter _inspectionReporter;
    private boolean _isOnWindows = true;

    public BuildService(InspectionReporter inspectionReporter) {
        _inspectionReporter = inspectionReporter;
    }

    @Override
    public void beforeProcessStarted() throws RunBuildException {
        Map<String, String> configParams = getConfigParameters();
        _isOnWindows = configParams.get("teamcity.agent.jvm.os.name").toLowerCase().startsWith("win");
    }

    @Override
    public void afterProcessFinished() throws RunBuildException {
        File[] files = MatchFiles();

        _inspectionReporter.markBuildAsInspectionsBuild();
        for (File file : files) {
            FxCopFileProcessor processor = new FxCopFileProcessor(file,
                    getCheckoutDirectory().toString(), getLogger(), _inspectionReporter);
            try {
                processor.processReport();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RunBuildException(e);
            }
        }
    }

    private File[] MatchFiles() {
        try {
            String[] pattern = new String[1];
            pattern[0] = "**/*.CodeAnalysisLog.xml";

            final AntPatternFileFinder finder = new AntPatternFileFinder(pattern, new String[0],
                    SystemInfo.isFileSystemCaseSensitive);

            final File artifactPath = getBuild().getCheckoutDirectory();
            getLogger().logMessage(DefaultMessagesInfo.createTextMessage("artiPath" + artifactPath.getAbsolutePath()));
            final File[] files = finder.findFiles(artifactPath);

            getLogger().logMessage(DefaultMessagesInfo.createTextMessage("Code Analysis Result Files:"));

            for (File file : files) {
                final String relativeName = FileUtil.getRelativePath(getWorkingDirectory(), file);

                getLogger().logMessage(DefaultMessagesInfo.createTextMessage("  " + relativeName));
            }

            if (files.length == 0) {
                getLogger().logMessage(DefaultMessagesInfo.createTextMessage("  none"));
            }

            return files;
        }
        catch(IOException ex){
            ex.printStackTrace();
            getLogger().logMessage(DefaultMessagesInfo.createTextMessage("  none"));
            return new File[0];
        }
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        return new ProgramCommandLine() {
            @NotNull
            public String getExecutablePath() throws RunBuildException {
                return _isOnWindows ? "cmd" : "sh";
            }

            @NotNull
            public String getWorkingDirectory() throws RunBuildException {
                return getCheckoutDirectory().getPath();
            }

            @NotNull
            public List<String> getArguments() throws RunBuildException {
                List<String> ret = new Vector<String>();
                if (_isOnWindows) {
                    ret.add("/c");
                }
                ret.add("echo");
                ret.add("Transforming CodeAnalysis Result...");
                return ret;
            }

            @NotNull
            public Map<String, String> getEnvironment() throws RunBuildException {
                return getBuildParameters().getEnvironmentVariables();
            }
        };
    }
}
