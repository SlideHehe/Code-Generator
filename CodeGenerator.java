import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * class for generating assembly code
 */
public class CodeGenerator {
    private final String readTreeFrom; // Path to tree
    private final String writeAsmTo; // Path to asm file
    private final ArrayList<TreeNode> registers; // List with registers to use in arithmetical expressions
    private PrintWriter printWriter; // PrintWriter class with .asm file opened

    public CodeGenerator(String readTreeFrom, String writeAsmTo) {
        this.readTreeFrom = readTreeFrom;
        this.writeAsmTo = writeAsmTo;
        this.registers = new ArrayList<>();
    }

    /**
     * function to call different parts of generation
     */
    public void generate() {
        StringToTree convert = new StringToTree(readTreeFrom);
        TreeNode root = convert.stringToTree();

        File writeAsm = new File(writeAsmTo);
        try {
            printWriter = new PrintWriter(writeAsm);

            generateHeader();
            boolean hasData = generateData(root.left);
            generateCode(root.right, hasData);

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void generateHeader() {
        printWriter.println("""
                .model small
                .stack 64
                .386
                """);
    }

    /**
     * declaring data segment
     * db (1 byte) for boolean values
     * dd (4 bytes) for integer values
     * all values of variables are unknown and marked with ?
     *
     * @param varNode pointer to variables subtree
     * @return false if data segment is empty,
     * so code segment won't update data segment
     * else returns true
     */
    private boolean generateData(TreeNode varNode) {
        if (varNode == null) return false;

        TreeNode currentNode = varNode.left;

        printWriter.println(".data");

        while (currentNode != null) {
            printWriter.print("\t" + currentNode.data + "\t");

            switch (currentNode.right.data) {
//                case "boolean" -> printWriter.println("db\t0");
                case "integer" -> printWriter.println("dd\t0");
                case "arrayinteger" -> {
                    printWriter.print("dd\t");
                    int size = Integer.parseInt(currentNode.right.left.data);
                    printWriter.println(size + " dup (0)");
                }
                default -> throw new RuntimeException("Runtime error");
            }

            currentNode = currentNode.left;
        }
        if (varNode.right != null) generateRegisters(varNode.right);

        printWriter.println();
        return true;
    }

    private void generateRegisters(TreeNode registersNode) {
        TreeNode currentNode = registersNode;

        while (currentNode != null) {
            registers.add(currentNode);
            printWriter.println("\t" + currentNode.data + "\tdd\t0");
            currentNode = currentNode.right;
        }
    }

    /**
     * declaring code segment
     *
     * @param beginNode pointer to code segment subtree
     * @param hasData   boolean value to check if data segments needs to be updated
     */
    private void generateCode(TreeNode beginNode, boolean hasData) {
        printWriter.println(".code\n" + beginNode.data + ":");
        if (hasData) {
            printWriter.println("""
                    \tmov ax,@Data
                    \tmov ds, ax
                    """);
        }
        generateCodeBody(beginNode.right);

        printWriter.println("program:\t" + "\n\tmov ax, 4C00h" + "\n\tint 21h" + "\n\n\tend " + beginNode.data);
    }

    /**
     * function to generate code body
     *
     * @param labelNode Node with code segment after begin
     */
    private void generateCodeBody(TreeNode labelNode) {
        if (labelNode == null) return;

        while (labelNode != null) {
            printWriter.println(labelNode.data + ":");

            switch (labelNode.left.data) {
                case ":=" -> generateAssignment(labelNode);
                case "while" -> generateWhile(labelNode);

                default -> throw new RuntimeException("Runtime error");
            }

            labelNode = labelNode.right;
        }
    }

    private void generateAssignment(TreeNode labelNode) {
        TreeNode identifierNode = labelNode.left;

        generateExpression(identifierNode.right, registers.iterator());

        if (identifierNode.left.left != null) {
            generateIndex(identifierNode.left.left, registers.iterator());
            identifierNode.left.data += "[esi]";
        }
        printWriter.println("\tmov " + identifierNode.left.data + ", eax\n");
    }

    /**
     * generating arithmetical expressions
     * used both for generating assignments and indexes
     *
     * @param identifierNode   node with index expression to the left
     * @param registerIterator iterator to iterate through list of registers for one single expression
     */
    private void generateExpression(TreeNode identifierNode, Iterator<TreeNode> registerIterator) {
        Stack<TreeNode> stack = new Stack<>();
        String arithmeticSigns = "-+*/";

        while (identifierNode != null) {
            if (arithmeticSigns.contains(identifierNode.data)) {
                TreeNode rightOperand = stack.pop();
                TreeNode leftOperand = stack.pop();

                if (leftOperand.left != null) {
                    generateIndex(leftOperand.left, registerIterator);
                    leftOperand.data += "[esi]";
                }
                printWriter.println("\tmov eax, " + leftOperand.data);

                if (rightOperand.left != null) {
                    generateIndex(rightOperand.left, registerIterator);
                    rightOperand.data += "[esi]";
                }
                switch (identifierNode.data) {
                    case "+" -> printWriter.println("\tadd eax, " + rightOperand.data);
                    case "-" -> printWriter.println("\tsub eax, " + rightOperand.data);
                    case "*" -> {
                        if (containsOnlyDigits(rightOperand.data)) {
                            printWriter.println("\tmov edx, " + rightOperand.data + "\n\timul edx");
                        } else {
                            printWriter.println("\timul " + rightOperand.data);
                        }
                    }
                    case "/" -> {
                        if (containsOnlyDigits(rightOperand.data)) {
                            printWriter.println("\tmov edx, " + rightOperand.data + "\n\tidiv edx");
                        } else {
                            printWriter.println("\tidiv " + rightOperand.data);
                        }
                    }
                    default -> throw new RuntimeException("Runtime error");
                }

                TreeNode currentRegister = registerIterator.next();

                if (identifierNode.right != null) {
                    printWriter.println("\tmov " + currentRegister.data + ", eax");
                    stack.push(currentRegister);
                }
            } else {
                if (containsOnlyDigits(identifierNode.data)) {
                    changeHexDigitsFirstLetter(identifierNode);
                } else {
                    changeIfNegative(identifierNode, registerIterator);
                }
                stack.push(identifierNode);
            }

            identifierNode = identifierNode.right;
        }

        if (!stack.empty()) printWriter.println("\tmov eax, " + stack.pop().data);
    }

    private void generateWhile(TreeNode labelNode) {
        TreeNode whileNode = labelNode.left;

        if (labelNode.right != null) generateCondition(whileNode.left, labelNode.right.data);
        else generateCondition(whileNode.left, "program"); // in case if while is last operand
        generateCodeBody(whileNode.right);

        printWriter.println("\tjmp " + labelNode.data + "\n");

    }

    /**
     * helper function to generate while operand
     *
     * @param conditionNode .data contains comparison sigh, .left - left side, .right - right
     * @param nextLabel     label where to move in case if while statement is false
     */
    private void generateCondition(TreeNode conditionNode, String nextLabel) {
        generateExpression(conditionNode.left, registers.iterator());
        printWriter.println("\tmov ebx, eax");

        generateExpression(conditionNode.right, registers.iterator());

        printWriter.println("\tmov ecx, eax" + "\n\tcmp ebx, ecx");

        switch (conditionNode.data) {
            case "=" -> printWriter.println("\tjne " + nextLabel);
            case "<>" -> printWriter.println("\tje " + nextLabel);
            case ">" -> printWriter.println("\tjle " + nextLabel);
            case "<" -> printWriter.println("\tjge " + nextLabel);
            case ">=" -> printWriter.println("\tjl " + nextLabel);
            case "<=" -> printWriter.println("\tjg " + nextLabel);

            default -> throw new RuntimeException("Runtime error");
        }

        printWriter.println();
    }

    private void generateIndex(TreeNode indexNode, Iterator<TreeNode> registerIterator) {
        printWriter.println("\tpush eax");

        generateExpression(indexNode, registerIterator);

        printWriter.println("\tmov edx, 4\n" + "\t imul edx");

        printWriter.println("\tmov esi, eax\n" + "\tpop eax");
    }

    /**
     * helper function to check if identifier is a number
     *
     * @param identifier string data
     * @return true if contains only digits (hex and decimal), else false
     */
    private boolean containsOnlyDigits(String identifier) {
        char firstSymbol = identifier.charAt(0);
        char lastSymbol = identifier.charAt(identifier.length() - 1);

        if (Character.isDigit(firstSymbol)) return true;

        return Character.digit(firstSymbol, 16) != -1 && lastSymbol == 'h';
    }

    /**
     * function to convert number to readable format for assembler
     * in case if it is variable and has minus sign in the string, function removes minus sing and calls neg function
     * changeHexDigitsFirstLetter is called to check if it is hex number and first symbol is not a digit,
     * in that case function adds '0' to the beginning of the string, so it won't conflict with assembler (e.g. Ah)
     *
     * @param identifierNode currently viewed number
     */
    private void changeIfNegative(TreeNode identifierNode, Iterator<TreeNode> registerIterator) {
        if (identifierNode.data.charAt(0) == '-') {
            identifierNode.data = identifierNode.data.substring(1);

            if (identifierNode.left != null) {
                generateIndex(identifierNode.left, registerIterator);
                printWriter.println("\tneg " + identifierNode.data + "[esi]");
            } else
                printWriter.println("\tneg " + identifierNode.data);
        }
    }

    private void changeHexDigitsFirstLetter(TreeNode hexIdentifierNode) {
        String hexLetters = "ABCDEF";
        if (hexLetters.contains(hexIdentifierNode.data.substring(0, 1))) {
            hexIdentifierNode.data = "0" + hexIdentifierNode.data;
        }
    }
}
