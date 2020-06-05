Lookup4j
============

Search by group:
`jbang https://github.com/orpiske/lookup4j/blob/master/lookup4j.java --group=org.apache.camel`

Search by group and filter by component:
`jbang https://github.com/orpiske/lookup4j/blob/master/lookup4j.java --group=org.apache.camel --artifact=camel-core`

Get the raw data easily into jq for more processing:
`jbang https://github.com/orpiske/lookup4j/blob/master/lookup4j.java --group=org.apache.camel --artifact=camel-core --raw | jq .`

Search by class name or full class name:
`jbang https://github.com/orpiske/lookup4j/blob/master/lookup4j.java --full-class=org.easymock.IArgumentMatcher --raw  | jq .`