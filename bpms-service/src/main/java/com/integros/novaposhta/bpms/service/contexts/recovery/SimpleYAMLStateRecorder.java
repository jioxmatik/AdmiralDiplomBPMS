package com.integros.novaposhta.bpms.service.contexts.recovery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

class SimpleYAMLStateRecorder implements IStateRecorder {
    /**
     * Path to state snapshot files relative to JBOSS_HOME
     */
    private static final String STATE_FILE_PATH = ".bpms-service/state";

    private final Yaml yaml;

    private final File currentStateFile;
    private final File previousStateFile;
    private final File stateFilesDir;

    public SimpleYAMLStateRecorder() throws InvalidStateFilesDirectoryException {
        this.yaml = new Yaml();
        this.stateFilesDir = getOrCreateStateFilesDir();
        this.previousStateFile = this.findLatestStateFile();
        this.currentStateFile = this.createCurrentStateFile();
    }

    private File findLatestStateFile() {
        int maxNumber = Integer.MIN_VALUE;
        File newestFile = null;
        for(File file : this.stateFilesDir.listFiles()) {
            int fileNumber = Integer.valueOf(file.getName());
            if(fileNumber > maxNumber) {
                maxNumber = fileNumber;
                newestFile = file;
            }
        }
        return newestFile;
    }

    private File createCurrentStateFile() {
        String filename;
        if(this.previousStateFile == null) {
            // No state files exist, create the first one
            filename = "0";
        } else {
            // Create a new sate file with the next number as its name
            filename = String.valueOf(Integer.valueOf(this.previousStateFile.getName()) + 1);
        }
        return new File(
            String.format(
                "%s/%s/%s", 
                getJbossHome(),
                STATE_FILE_PATH,
                filename
            )
        );
    }

    private static File getOrCreateStateFilesDir() throws InvalidStateFilesDirectoryException {
        String path = getJbossHome() + "/" + STATE_FILE_PATH;
        File result = new File(path);
        if(!result.exists() && !result.mkdirs()) {
            throw new InvalidStateFilesDirectoryException(
                String.format(
                    "Directory %s does not exist and couldn't be created, ensure that permissions on parent directories are set correctly",
                    path
                )
            );
        } else if(!result.isDirectory()){
            throw new InvalidStateFilesDirectoryException(
                String.format(
                    "%s already exists and is not a directory",
                    path
                )
            );
        }
        return result;
    }
    
    private static String getJbossHome() {
        return System.getenv("JBOSS_HOME");
    }

    @Override
    public IServiceContextManagerState getPreviousSessionState() throws StateRecoveryFailedException {
        if(this.previousStateFile == null) {
            throw new StateRecoveryFailedException(
                String.format(
                    "Previous state file not found in %s",
                    this.stateFilesDir.getAbsolutePath()
                )
            );
        }
        return this.loadStateFromFile(this.previousStateFile);
    }

    @Override
    public IServiceContextManagerState getLastRecordedState() throws StateRecoveryFailedException {
        assert this.currentStateFile != null : String.format(
            "currentStateFile field cannot be null in a %s object",
            this.getClass().getName()
        );
        return this.loadStateFromFile(this.currentStateFile);
    }

    @Override
    public void recordState(IServiceContextManagerState state) throws StateRecordingFailedException {        
        try {
            yaml.dump(state.getContextAliasMap(), new OutputStreamWriter(new FileOutputStream(this.currentStateFile)));
        } catch (FileNotFoundException e) {
            throw new StateRecordingFailedException(e);
        }
    }
    
    protected IServiceContextManagerState loadStateFromFile(File file) throws StateRecoveryFailedException {
        try {
            Map<String, Set<String>> contextAliasMap = (Map<String, Set<String>>) yaml.load(new FileInputStream(file));
            if(contextAliasMap == null) {
                throw new RuntimeException(
                    String.format(
                        "Failed to parse %s as YAML",
                        file.getAbsolutePath()
                    )
                );
            }
            return new ImmutableManagerState(contextAliasMap);
        } catch (Exception e) {
            throw new StateRecoveryFailedException(e);
        }
    }
}
