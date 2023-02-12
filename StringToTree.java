import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * class to read binary tree from parser to TreeNode
 */
public class StringToTree {
    private int index = 0;
    private String treeString;

    public StringToTree(String pathToFile) {
        File file = new File(pathToFile);

        try {
            Scanner scanner = new Scanner(file);
            treeString = scanner.nextLine();

            scanner.close();
        } catch (FileNotFoundException e) {
            treeString = null;
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public TreeNode stringToTree() {
        if (treeString == null || treeString.length() == 0) return null;

        return stringToTreeHelper();
    }

    /**
     * function to construct tree from file
     * index is used to track current position in string
     * then function is recursively called to add nodes to tree
     *
     * @return newly build tree node with data found between brackets
     */
    private TreeNode stringToTreeHelper() {
        if (index >= treeString.length()) return null;

        StringBuilder currentString = new StringBuilder();

        while (index < treeString.length() && treeString.charAt(index) != '{' &&
                treeString.charAt(index) != '}') {
            currentString.append(treeString.charAt(index));
            index++;
        }

        TreeNode root = new TreeNode(currentString.toString());

        if (currentString.isEmpty()) root = null;

        if (index >= treeString.length()) return root;

        if (treeString.charAt(index) == '{') {
            index++;
            if (root != null) root.left = stringToTreeHelper();
        }

        if (index < treeString.length() && treeString.charAt(index) == '}') {
            index++;
            return root;
        }

        if (treeString.charAt(index) == '{') {
            index++;
            if (root != null) root.right = stringToTreeHelper();
        }

        if (index < treeString.length() && treeString.charAt(index) == '}') {
            index++;
            return root;
        }

        return root;
    }
}
