/**
 * <pre>
 * neo4j.conf:
 *    #Comma separated list of JAXRS packages containing JAXRS Resource, one package name for each mountpoint.
 *    dbms.unmanaged_extension_classes=org.neo4j.examples.server.unmanaged=/examples/unmanaged
 * 
 * 插件目录:
 * $NEO4J_HOME/plugins
 * 
 * 访问: 
 * curl http://localhost:7474/examples/unmanaged/helloworld/123
 * </pre>
 */
package com.spike.giantdataanalysis.neo4j.example.extension;