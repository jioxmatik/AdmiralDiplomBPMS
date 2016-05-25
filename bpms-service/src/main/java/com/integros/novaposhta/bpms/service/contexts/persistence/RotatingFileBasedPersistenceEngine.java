package com.integros.novaposhta.bpms.service.contexts.persistence;

import com.integros.novaposhta.bpms.service.contexts.IServiceContext;
import com.integros.novaposhta.bpms.service.contexts.ServiceContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.marshalling.Marshaller;
import org.kie.api.runtime.KieSession;
import org.yaml.snakeyaml.Yaml;

public class RotatingFileBasedPersistenceEngine implements IServiceContextPersistenceEngine {
    private static final String TIMESTAMP_SEPARATOR = "_";
    private final String METADATA_FILENAME = "meta.yaml";

    private final String persistencePath;
    protected final File persistenceDirectory;
    
    private final Yaml yaml = new Yaml();
    private Map<String, MetaPersistence> meta = new HashMap<String, MetaPersistence>();
	private File metaFile;
    
    public RotatingFileBasedPersistenceEngine(String persistencePath) throws InvalidPersistencePathException
    {
        this.persistencePath = persistencePath;
        try {
            this.persistenceDirectory = new File(this.persistencePath);
        } catch(Exception e) {
            throw new InvalidPersistencePathException(this.persistencePath, e);
        }
        if(!this.persistenceDirectory.exists() || !this.persistenceDirectory.isDirectory()) {
            throw new InvalidPersistencePathException(
                this.persistencePath,
                String.format("%s must be a directory", this.persistencePath)
            );
        }
        
        this.metaFile = new File(persistenceDirectory, METADATA_FILENAME);
        metaFile.getParentFile().mkdirs(); 
		try {
			metaFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void persist(String key, IServiceContext value) throws ServiceContextPersistenceFailedException {
        ByteArrayOutputStream baos = null;
        OutputStream os = null;
        try {
            KieSession kSession = value.getKieSession();
            baos = new ByteArrayOutputStream();
            os = new FileOutputStream(this.getFilename(key));
            Marshaller marshaller = KieServices.Factory.get().getMarshallers().newMarshaller(kSession.getKieBase());
            marshaller.marshall(baos, kSession);
            baos.writeTo(os);
            writeMetaData(key, value.getReleaseId(), value.getSessionName());
        } catch(Exception e) {
            throw new ServiceContextPersistenceFailedException(e);
        } finally {
            if(baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IServiceContext restore(String key) throws ServiceContextRestorationFailedException {
        try {
        	MetaPersistence metaData = getMetaData(key);
        	return restoreToContext(key, new ServiceContext(metaData.getGroupId(), metaData.getArtifactId(), metaData.getVersion(), metaData.getSessionName()));
        } catch(Exception e) {
            throw new ServiceContextRestorationFailedException(e);
        }
    }

    @Override
    public IServiceContext restoreToContext(String key, IServiceContext target) throws ServiceContextRestorationFailedException {
        InputStream is = null;
        try{
            Marshaller marshaller = KieServices.Factory.get().getMarshallers().newMarshaller(target.getKieSession().getKieBase());
            is = new FileInputStream(this.getLatestDump(key));
            target.reset(marshaller.unmarshall(is));
            return target;
        } catch(Exception e) {
            throw new ServiceContextRestorationFailedException(e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean containsKey(String key) {
        for(File file : this.persistenceDirectory.listFiles()) {
            if(file.getName().startsWith(key)) {
                return true;
            }
        }
        return false;
    }
    
    protected String getFilename(String key) {
        return this.persistencePath + "/" + key + TIMESTAMP_SEPARATOR + getTimestampString();
    }
    
    protected String getTimestampString() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    protected File getLatestDump(String key) {
        long maxTimestamp = 0;
        File newestFile = null;
        for(File file : this.persistenceDirectory.listFiles()) {
            String filename = file.getName();
            if(filename.startsWith(key)) {
                long fileTimestamp = Long.valueOf(filename.substring(filename.indexOf(TIMESTAMP_SEPARATOR)+1));
                if(fileTimestamp > maxTimestamp) {
                    maxTimestamp = fileTimestamp;
                    newestFile = file;
                }
            }
        }
        return newestFile;
    }

    @Override
    public Set<String> keySet() throws PersistenceNotAvailableException {
        Set<String> result = new HashSet<String>();
        for(File file : this.persistenceDirectory.listFiles()) {
            String filename = file.getName();
            int separatorIndex = filename.indexOf(TIMESTAMP_SEPARATOR);
            if(separatorIndex < 0) {
                continue;
            }
            String key = filename.substring(0, separatorIndex);
            result.add(key);
        }
        return result;
    }
    
    private void writeMetaData(String key, ReleaseId gav, String sessionName) throws ServiceContextRestorationFailedException{
    	try {
			meta = (Map<String, MetaPersistence>) yaml.load(new FileInputStream(this.metaFile));
			if(meta == null)
				meta = new HashMap<String, MetaPersistence>();
			meta.put(key, new MetaPersistence(sessionName, gav));
			yaml.dump(meta, new OutputStreamWriter(new FileOutputStream(metaFile)));
		} catch (FileNotFoundException e) {
            throw new ServiceContextRestorationFailedException(e);
		} catch (ClassCastException e){
            throw new ServiceContextRestorationFailedException(e);
		}
    }
    
    private MetaPersistence getMetaData(String key) throws ServiceContextRestorationFailedException{
    	try {
			meta = (Map<String, MetaPersistence>) yaml.load(new FileInputStream(this.metaFile));
			if(meta == null)
				throw new ServiceContextRestorationFailedException("Couldn't parse YAML file: "+this.metaFile.getAbsolutePath());
			return meta.get(key);
		} catch (FileNotFoundException e) {
            throw new ServiceContextRestorationFailedException(e);
		} catch (ClassCastException e){
            throw new ServiceContextRestorationFailedException(e);
		}
    }
    
}
