package org.neo4j.visualization.graphviz;

import java.io.IOException;
import java.util.Iterator;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

public class MyNodeStyle extends SimpleNodeStyle {

  public MyNodeStyle(DefaultStyleConfiguration configuration) {
    super(configuration);
  }

  @Override
  public void emitNodeStart(Appendable stream, Node node) throws IOException {
    stream.append("  N" + node.getId() + " [\n");
    config.emit(node, stream);
    stream.append("    label = \"");
    Iterator<Label> labels = node.getLabels().iterator();
    hasLabels = labels.hasNext();
    if (hasLabels) {
      hasLabels = labels.hasNext();
      if (hasLabels) {
        stream.append("{");
        while (labels.hasNext()) {
          stream.append(labels.next().name());
          if (labels.hasNext()) {
            stream.append(", ");
          }
        }
        stream.append(" (" + String.valueOf(node.getId()) + ")"); // DIFF
        stream.append("|");
      }
    }
  }
}
