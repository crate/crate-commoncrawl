package io.crate.plugin.commoncrawl;


import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class WETFileProcessor {
    private static final Logger logger = Loggers.getLogger(WETFileProcessor.class);

    private static final String blockDelimiter = "WARC/1.0";

    private static final String warcTypeHeader = "WARC-Type:";
    private static final String warcDateHeader = "WARC-Date:";
    private static final String warcTargetUriHeader = "WARC-Target-URI:";
    private static final String contentTypeHeader = "Content-Type:";
    private static final String contentLengthHeader = "Content-Length:";


    private static final byte NEW_LINE = (byte) '\n';
    private int skipped;
    private int recordsWritten;

    XContentBuilder builder;

    BufferedReader sourceReader;
    OutputStream outputStream;

    String line;


    public WETFileProcessor(BufferedReader sourceReader, OutputStream outputStream) throws IOException {
        this.outputStream = outputStream;
        builder = XContentFactory.jsonBuilder(this.outputStream);
        this.sourceReader = sourceReader;
        this.recordsWritten = 0;
        this.skipped = 0;
    }


    private boolean nextLine() throws IOException {
        line = sourceReader.readLine();
        return line != null;
    }

    private String getUntil(String prefix) throws IOException {
        StringBuilder content = new StringBuilder();
        do {
            if (line.startsWith(prefix)) {
                break;
            }
            content.append(line);
            content.append(NEW_LINE);
        } while (nextLine());
        return content.toString();
    }

    private String skipUntil(String prefix) throws IOException {
        do {
            if (line.startsWith(prefix)) {
                return line;
            }
        } while (nextLine());
        return null;
    }

    private String removePrefix(String prefixPattern) {
        return line.replaceFirst(prefixPattern, "").trim();
    }

    private void writeObject(String reverseDomain, Boolean schema, String path, String date, String contentType, Integer contentLength, String content) throws IOException {
        builder.startObject();
        builder.field("authority", reverseDomain);
        builder.field("ssl", schema.toString());
        builder.field("path", path);
        builder.field("date", date);
        builder.field("ctype", contentType);
        builder.field("clen", contentLength);
        builder.field("content", content);
        builder.endObject();
        builder.flush();
        logger.debug("{ authority: " + reverseDomain + ", ssl: " + schema + ", path: " + path + ", date: " + date + " }");
        outputStream.write(NEW_LINE);

        this.recordsWritten++;
    }

    public void processToStream() throws IOException {
        while (this.nextLine()) {
            skipUntil(warcTypeHeader);

            if (removePrefix(warcTypeHeader).equalsIgnoreCase("conversion"))  {
                skipUntil(warcTargetUriHeader);
                String uriStr = removePrefix(warcTargetUriHeader);
                skipUntil(warcDateHeader);
                String zonedDate = removePrefix(warcDateHeader);
                skipUntil(contentTypeHeader);
                String contentType = removePrefix(contentTypeHeader);
                skipUntil(contentLengthHeader);
                Integer contentLength = Integer.parseInt(removePrefix(contentLengthHeader));
                nextLine();
                String content = getUntil(blockDelimiter).trim();
                URI uri;
                try {
                    uri = new URI(uriStr);
                } catch (URISyntaxException e) {
                    continue;
                }

                StringBuilder reverseDomainBuilder = new StringBuilder();
                String[] splitDomain = uri.getHost().split("\\.");
                for (int i = splitDomain.length - 1; i >= 0; i--) {
                    reverseDomainBuilder.append(splitDomain[i]);
                    if (i > 0) {
                        reverseDomainBuilder.append(".");
                    }
                }

                String reverseDomain = reverseDomainBuilder.toString() +
                                       (uri.getPort() > 0 ? ":" + uri.getPort() : "");

                StringBuilder pathBuilder = new StringBuilder(uri.getPath() == null ? "/" : uri.getPath());
                pathBuilder.append(uri.getQuery() != null ? "?" + uri.getQuery() : "");
                pathBuilder.append(uri.getFragment() != null ? "#" + uri.getFragment() : "");

                writeObject(reverseDomain,
                        uri.getScheme() == "https",
                        pathBuilder.toString(), zonedDate, contentType, contentLength, content);
            } else skipped++;
        }

    }

    public int getRecordsWritten() {
        return recordsWritten;
    }

    public int getSkipped() {
        return skipped;
    }
}
