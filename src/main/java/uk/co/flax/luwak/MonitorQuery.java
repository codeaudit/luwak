package uk.co.flax.luwak;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import org.apache.lucene.util.BytesRef;

/**
 * Copyright (c) 2013 Lemur Consulting Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Defines a query to be stored in a Monitor
 */
public class MonitorQuery {

    private final String id;
    private final String query;
    private final String highlightQuery;
    private final ImmutableMap<String, String> metadata;

    /**
     * Creates a new MonitorQuery
     * @param id the ID
     * @param query the query to store
     * @param highlightQuery an optional query to use for highlighting.  May be null.
     */
    public MonitorQuery(String id, String query, String highlightQuery, Map<String, String> metadata) {
        this.id = id;
        this.query = query;
        this.highlightQuery = highlightQuery;
        this.metadata = ImmutableMap.copyOf(metadata);
    }

    /**
     * Creates a new MonitorQuery with no highlight query
     */
    public MonitorQuery(String id, String query, Map<String, String> metadata) {
        this(id, query, null, metadata);
    }

    public MonitorQuery(String id, String query, String highlight) {
        this(id, query, highlight, ImmutableMap.<String, String>of());
    }

    /**
     * Creates a new MonitorQuery, with no highlight query and no metadata
     * @param id the ID
     * @param query the query to store
     */
    public MonitorQuery(String id, String query) {
        this(id, query, null, ImmutableMap.<String, String>of());
    }

    /**
     * @return this MonitorQuery's ID
     */
    public String getId() {
        return id;
    }

    /**
     * @return this MonitorQuery's query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @return this MonitorQuery's highlight query.  May be null.
     */
    public String getHighlightQuery() {
        return highlightQuery;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonitorQuery that = (MonitorQuery) o;

        if (highlightQuery != null ? !highlightQuery.equals(that.highlightQuery) : that.highlightQuery != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (highlightQuery != null ? highlightQuery.hashCode() : 0);
        return result;
    }

    public BytesRef hash() {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(query.getBytes(Charsets.UTF_8));
            if (highlightQuery != null)
                md5.update(highlightQuery.getBytes(Charsets.UTF_8));
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                md5.update(entry.getKey().getBytes(Charsets.UTF_8));
                md5.update(entry.getValue().getBytes(Charsets.UTF_8));
            }
            return new BytesRef(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't use MD5 hash on this system", e);
        }
    }
}
