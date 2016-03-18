package io.crate.plugin.commoncrawl;

import com.google.common.base.Predicate;
import io.crate.operation.collect.files.FileInput;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class WETFileInput implements FileInput {

    private static final ESLogger logger = Loggers.getLogger(WETFileInput.class);

    @Override
    public List<URI> listUris(URI fileUri, Predicate<URI> uriPredicate) throws IOException {
        return Collections.singletonList(fileUri);
    }

    @Override
    public InputStream getStream(URI uri) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(uri.toString().replaceFirst("ccrawl", "http"));
        try {
            inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new GzipCompressorInputStream(inputStream, true), StandardCharsets.UTF_8));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(262144000);
            WETFileProcessor wetFileProcessor = new WETFileProcessor(reader, outputStream);
            wetFileProcessor.processToStream();
            byte[] commoncrawlJson = outputStream.toByteArray();
            outputStream.close();
            logger.info("Got " + wetFileProcessor.getRecordsWritten() + " objects, skipped " + wetFileProcessor.getSkipped());
            return new ByteArrayInputStream(commoncrawlJson);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

    @Override
    public boolean sharedStorageDefault() {
        return true;
    }
}
