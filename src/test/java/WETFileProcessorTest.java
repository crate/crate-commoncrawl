import io.crate.plugin.commoncrawl.WETFileProcessor;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class WETFileProcessorTest extends TestCase{

    class ByteArrayOutputStreamWithExposedBuffer extends ByteArrayOutputStream {
        public ByteArrayOutputStreamWithExposedBuffer(int size) {
            super(size);
        }

        public byte[] getBuffer() {
            return buf;
        }
    }

    @Test
    public void testProcessWETFile() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResource("/data/example.wet.gz").openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream),
                    StandardCharsets.UTF_8));
            ByteArrayOutputStreamWithExposedBuffer outputStream = new ByteArrayOutputStreamWithExposedBuffer(209700000);
            WETFileProcessor wetFileProcessor = new WETFileProcessor(reader, outputStream);
            wetFileProcessor.processToStream();

            ByteArrayInputStream resultInputStream = new ByteArrayInputStream(outputStream.getBuffer());

            BufferedReader resultReader = new BufferedReader(new InputStreamReader(resultInputStream, StandardCharsets.UTF_8));
            assertThat(resultReader.readLine(), startsWith("{\"authority\":\"be.0x20\",\"ssl\":\"false\",\"path\":\""));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
