// Execution Mode

// run: bin/gremlin.sh -e custom-groovy-scripts/modern-execution.groovy

modernGraph = TinkerFactory.createModern()
g = modernGraph.traversal()
println "Modern Graph loaded."

g.V(1).out('knows').has('age', gt(30)).values('name').each{ println it }
