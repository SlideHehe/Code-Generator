public class Main {
    /**
     * main function to call code generator
     *
     * @param args path to files
     *             [0] - .txt file with binary tree from parser
     *             [1] - .asm file where assembly code will be written
     */
    public static void main(String[] args) {
        CodeGenerator codeGenerator;

        if (args.length == 0) {
            codeGenerator = new CodeGenerator("tree.txt", "program.asm");
        } else {
            codeGenerator = new CodeGenerator(args[0], args[1]);
        }

        codeGenerator.generate();
    }
}
