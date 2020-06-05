//usr/bin/env jbang "$0" "$@" ; exit $?
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//DEPS info.picocli:picocli:4.2.0
//DEPS org.apache.cxf:cxf-rt-rs-client:3.3.1
//DEPS org.apache.cxf:cxf-rt-transports-http-hc:3.3.1
//DEPS com.fasterxml.jackson.core:jackson-databind:2.9.9
//DEPS com.fasterxml.jackson.core:jackson-annotations:2.9.9
//DEPS com.fasterxml.jackson.core:jackson-core:2.9.9

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;



@Command(name = "lookup4j", mixinStandardHelpOptions = true, version = "lookup4j 0.1",
        description = "lookup4j made with jbang")
class lookup4j implements Callable<Integer> {
    public static class MavenSearchResponseDoc {
        private String id;
        @JsonProperty(value = "g")
        private String group;
        @JsonProperty(value = "a")
        private String artifact;
        private String latestVersion;
        private String repositoryId;
        private String p;
        @JsonIgnore
        private long timestamp;
        @JsonIgnore
        private int versionCount;
        @JsonIgnore
        private Object text;
        @JsonIgnore
        private Object ec;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getArtifact() {
            return artifact;
        }

        public void setArtifact(String artifact) {
            this.artifact = artifact;
        }

        public String getLatestVersion() {
            return latestVersion;
        }

        public void setLatestVersion(String latestVersion) {
            this.latestVersion = latestVersion;
        }

        public String getRepositoryId() {
            return repositoryId;
        }

        public void setRepositoryId(String repositoryId) {
            this.repositoryId = repositoryId;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getVersionCount() {
            return versionCount;
        }

        public void setVersionCount(int versionCount) {
            this.versionCount = versionCount;
        }

        public Object getText() {
            return text;
        }

        public void setText(Object text) {
            this.text = text;
        }

        public Object getEc() {
            return ec;
        }

        public void setEc(Object ec) {
            this.ec = ec;
        }
    }

    public static class MavenSearchResponseData {
        private int numFound;
        private int start;

        private List<MavenSearchResponseDoc> docs = new ArrayList<>();

        public int getNumFound() {
            return numFound;
        }

        public void setNumFound(int numFound) {
            this.numFound = numFound;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public List<MavenSearchResponseDoc> getDocs() {
            return docs;
        }

        public void setDocs(List<MavenSearchResponseDoc> docs) {
            this.docs = docs;
        }
    }

    public static class MavenSearchResponse {
        @JsonIgnore
        private Object responseHeader;

        private MavenSearchResponseData response;

        @JsonIgnore
        private Object spellcheck;

        public Object getResponseHeader() {
            return responseHeader;
        }

        public void setResponseHeader(Object responseHeader) {
            this.responseHeader = responseHeader;
        }

        public MavenSearchResponseData getResponse() {
            return response;
        }

        public void setResponse(MavenSearchResponseData response) {
            this.response = response;
        }

        public Object getSpellcheck() {
            return spellcheck;
        }

        public void setSpellcheck(Object spellcheck) {
            this.spellcheck = spellcheck;
        }
    }

    public static WebClient url(final String requestUrl) {
        WebClient webClient = WebClient.create(requestUrl);

        webClient
                .type(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE);

        return webClient;
    }


    @Option(names = {"-g", "--group"}, description = "The group to search", defaultValue = "")
    private String group;

    @Option(names = {"-a", "--artifact"},  description = "The artifact to search", defaultValue = "")
    private String artifact;

    @Option(names = {"--debug"}, description = "Use debug mode")
    private boolean debug = false;

    @Option(names = {"--raw"}, description = "Show raw data")
    private boolean raw = false;

    @Option(names = {"--class"}, description = "Search by class name", defaultValue = "")
    private String className;

    @Option(names = {"--full-class"}, description = "Search by full class name", defaultValue = "")
    private String fullClassName;

    public static void main(String... args) {
        int exitCode = new CommandLine(new lookup4j()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        StringBuilder sb = new StringBuilder("http://search.maven.org/solrsearch/select?wt=json&q=");

        if (!group.isEmpty()) {
            sb.append("g:\"");
            sb.append(group);
            sb.append("\"");
        }

        if (!artifact.isEmpty()) {
            if (!group.isEmpty()) {
                sb.append("+AND+");
            }

            sb.append("a:\"");
            sb.append(artifact);
            sb.append("\"");
        }

        if (!className.isEmpty()) {
            sb.append("c:\"");
            sb.append(className);
            sb.append("\"");
        }

        if (!fullClassName.isEmpty()) {
            sb.append("fc:\"");
            sb.append(fullClassName);
            sb.append("\"");
        }


        String request = sb.toString();

        if (debug) {
            System.out.println("Sending request to: " + request);
        }

        Response response = url(request).get();

        MappingJsonFactory factory = new MappingJsonFactory();
        try {
            InputStream stream = (InputStream) response.getEntity();

            if (raw) {
                System.out.println(IOUtils.readStringFromStream(stream));
            }

            JsonParser parser = factory.createParser(stream);

            MavenSearchResponse mavenSearchResponse = parser.readValueAs(MavenSearchResponse.class);

            String mavenDepTxt = "<dependency>\n\t<groupId>%s</groupId>\n\t<artifactId>%s</artifactId>\n\t<version>%s</version>\n</dependency>\n";
            for (MavenSearchResponseDoc doc : mavenSearchResponse.getResponse().getDocs()) {
                System.out.println(String.format("============ %s:%s", doc.getId(), doc.getLatestVersion()));
                System.out.println(String.format(mavenDepTxt, doc.getGroup(), doc.getArtifact(), doc.getLatestVersion()));
            }

        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
        return 0;
    }
}
