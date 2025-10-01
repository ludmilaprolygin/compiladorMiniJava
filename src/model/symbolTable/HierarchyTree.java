package model.symbolTable;

import java.util.ArrayList;
import java.util.List;

public class HierarchyTree {
    String className;
    List<HierarchyTree> descendants;

    public HierarchyTree(String className) {
        this.className = className;
        this.descendants = new ArrayList<>();
    }

    public void addDescendant(HierarchyTree hijo) {
        if(descendants != null)
            descendants.add(hijo);
    }

    public HierarchyTree search(String target) {
        if (className.equals(target)) {
            return this;
        }
        for (HierarchyTree child : descendants) {
            HierarchyTree found = child.search(target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public String toString() {
        return toString("");
    }

    private String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(className).append("\n");  // o className en tu caso

        for (HierarchyTree child : descendants) {
            sb.append(child.toString(indent + "  "));
        }

        return sb.toString();
    }

    public boolean isAncestor(String ancestor, String descendant) {
        HierarchyTree ancestorNode = search(ancestor);
        if (ancestorNode == null) return false;

        return ancestorNode.search(descendant) != null;
    }
}