// Interactive Mode

// run: bin/gremlin.sh -i custom-groovy-scripts/modern-interactive.groovy <parameter>
// example: bin/gremlin.sh -i custom-groovy-scripts/modern-interactive.groovy marko

graph = TinkerFactory.createModern()
g = graph.traversal()
g.V().has('name',args[0]).each { println it }
