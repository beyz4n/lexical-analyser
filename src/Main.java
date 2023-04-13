import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Main {

    // Variables to keep track of row and column values
    public static int row = 1;
    public static int col = 0;

    public static String tokens = ""; // Variable to keep all messages for tokens
    public static PrintWriter printer; // Declaration for PrintWriter in order to print to the output file

    public static void main(String[] args) throws FileNotFoundException {

        // Prompt to user to write the file name.
        System.out.print("Enter the name of the input file: ");

        // To take name of input file from the user.
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        File file = new File(input);

        // Create a file for output.
        File output = new File("output.txt");
        printer = new PrintWriter(output);

        // If given file name does not match with the existing files, prompt user to write the file name correctly.
        while (!file.exists()) {
            System.out.print("You entered a file that does not exist. Please enter the name of the input file correctly: ");
            sc = new Scanner(System.in);
            input = sc.nextLine();
            file = new File(input);
        }

        // Scanner to read inside the given file
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) { // Check if input file has next line
            String line = scanner.nextLine(); // Variable to keep one line of the code
            col = 0;
            while (line.length() > col) { // Check if end of the line
                char currentCh = line.charAt(col); // Variable to keep character at that column
                if (currentCh == ' ') { // Eliminate spaces
                    col++;
                } else if (currentCh == '~') { // Don't read what's on that line after tilde sign (~)
                    col = 0;
                    break;

                // Detect brackets
                } else if (currentCh == '(') {
                    col++;
                    tokens += "LEFTPAR " + row + ":" + col + "\n";
                } else if (currentCh == ')') {
                    col++;
                    tokens += "RIGHTPAR " + row + ":" + col + "\n";
                } else if (currentCh == '[') {
                    col++;
                    tokens += "LEFTSQUAREB " + row + ":" + col + "\n";
                } else if (currentCh == ']') {
                    col++;
                    tokens += "RIGHTSQUAREB " + row + ":" + col + "\n";
                } else if (currentCh == '{') {
                    col++;
                    tokens += "LEFTCURLYB " + row + ":" + col + "\n";
                } else if (currentCh == '}') {
                    col++;
                    tokens += "RIGHTCURLYB " + row + ":" + col + "\n";
                    //HAVING + - or . at the beginning
                } else if ((currentCh == '.' || currentCh == '+' || currentCh == '-')) {
                    String str = "" + currentCh;
                    int currentCol = col + 1;
                    while (currentCol != line.length() && line.charAt(currentCol) != ' ' && line.charAt(currentCol) != '(' && line.charAt(currentCol) != ')' && line.charAt(currentCol) != '[' && line.charAt(currentCol) != ']' && line.charAt(currentCol) != '{' && line.charAt(currentCol) != '}' && line.charAt(currentCol) != '~') {
                        str += line.charAt(currentCol);
                        currentCol++;
                    }
                    if (str.length() == 1) {
                        tokens += "IDENTIFIER " + row + ":" + (col + 1) + "\n";
                        col++;
                    } else if ((currentCh == '+' || currentCh == '-') && (line.charAt(col + 1) == '.' || (line.charAt(col + 1) >= '0' && line.charAt(col + 1) <= '9'))) {
                        isNumber(line, currentCh);
                    } else if (currentCh == '.' && (line.charAt(col + 1) >= '0' && line.charAt(col + 1) <= '9')) {
                        isNumber(line, currentCh);
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                        printer.close();
                        System.exit(1);
                    }
                    //IDENTIFIER & KEYWORDS & BOOLEAN
                } else if ((currentCh >= 'a' && currentCh <= 'z') || currentCh == '!' || currentCh == '*' || currentCh == '/' || currentCh == ':' || currentCh == '<' || currentCh == '>' || currentCh == '=' || currentCh == '?') {
                    //KEYWORDS
                    if (line.substring(col).length() >= 4 && line.substring(col, col + 4).equals("true")) {
                        if (line.substring(col).length() == 4) {
                            tokens += "BOOLEAN " + row + ":" + (col + 1) + "\n";
                            col = col + 4;
                        } else if (line.charAt(col + 4) == ' ' || line.charAt(col + 4) == '~' || line.charAt(col + 4) == '(' || line.charAt(col + 4) == ')' || line.charAt(col + 4) == '[' || line.charAt(col + 4) == ']' || line.charAt(col + 4) == '{' || line.charAt(col + 4) == '}' || col + 4 == line.length() - 1) {
                            tokens += "BOOLEAN " + row + ":" + (col + 1) + "\n";
                            col = col + 4;
                        }
                    } else if (line.substring(col).length() >= 5 && line.substring(col, col + 5).equals("false")) {
                        if (line.substring(col).length() == 5) {
                            tokens += "BOOLEAN " + row + ":" + (col + 1) + "\n";
                            col = col + 5;
                        } else if (line.charAt(col + 5) == ' ' || line.charAt(col + 5) == '~' || line.charAt(col + 5) == '(' || line.charAt(col + 5) == ')' || line.charAt(col + 5) == '[' || line.charAt(col + 5) == ']' || line.charAt(col + 5) == '{' || line.charAt(col + 5) == '}' || col + 5 == line.length() - 1) {
                            tokens += "BOOLEAN " + row + ":" + (col + 1) + "\n";
                            col = col + 5;
                        }
                    } else if (line.substring(col).length() >= 6 && line.substring(col, col + 6).equals("define")) {
                        if (line.substring(col).length() == 6) {
                            tokens += "DEFINE " + row + ":" + (col + 1) + "\n";
                            col = col + 6;
                        } else if (line.charAt(col + 6) == ' ' || line.charAt(col + 6) == '~' || line.charAt(col + 6) == '(' || line.charAt(col + 6) == ')' || line.charAt(col + 6) == '[' || line.charAt(col + 6) == ']' || line.charAt(col + 6) == '{' || line.charAt(col + 6) == '}' || col + 6 == line.length() - 1) {
                            tokens += "DEFINE " + row + ":" + (col + 1) + "\n";
                            col = col + 6;
                        }
                    } else if (line.substring(col).length() >= 3 && line.substring(col, col + 3).equals("let")) {
                        if (line.substring(col).length() == 3) {
                            tokens += "LET " + row + ":" + (col + 1) + "\n";
                            col = col + 3;
                        } else if (line.charAt(col + 3) == ' ' || line.charAt(col + 3) == '~' || line.charAt(col + 3) == '(' || line.charAt(col + 3) == ')' || line.charAt(col + 3) == '[' || line.charAt(col + 3) == ']' || line.charAt(col + 3) == '{' || line.charAt(col + 3) == '}' || col + 3 == line.length() - 1) {
                            tokens += "LET " + row + ":" + (col + 1) + "\n";
                            col = col + 3;
                        }
                    } else if (line.substring(col).length() >= 4 && line.substring(col, col + 4).equals("cond")) {
                        if (line.substring(col).length() == 4) {
                            tokens += "COND " + row + ":" + (col + 1) + "\n";
                            col = col + 4;
                        } else if (line.charAt(col + 4) == ' ' || line.charAt(col + 4) == '~' || line.charAt(col + 4) == '(' || line.charAt(col + 4) == ')' || line.charAt(col + 4) == '[' || line.charAt(col + 4) == ']' || line.charAt(col + 4) == '{' || line.charAt(col + 4) == '}' || col + 4 == line.length() - 1) {
                            tokens += "COND " + row + ":" + (col + 1) + "\n";
                            col = col + 4;
                        }
                    } else if (line.substring(col).length() >= 2 && line.substring(col, col + 2).equals("if")) {
                        if (line.substring(col).length() == 2) {
                            tokens += "IF " + row + ":" + (col + 1) + "\n";
                            col = col + 2;
                        } else if (line.charAt(col + 2) == ' ' || line.charAt(col + 2) == '~' || line.charAt(col + 2) == '(' || line.charAt(col + 2) == ')' || line.charAt(col + 2) == '[' || line.charAt(col + 2) == ']' || line.charAt(col + 2) == '{' || line.charAt(col + 2) == '}' || col + 2 == line.length() - 1) {
                            tokens += "IF " + row + ":" + (col + 1) + "\n";
                            col = col + 2;
                        }
                    } else if (line.substring(col).length() >= 5 && line.substring(col, col + 5).equals("begin")) {
                        if (line.substring(col).length() == 5) {
                            tokens += "BEGIN " + row + ":" + (col + 1) + "\n";
                            col = col + 5;
                        } else if (line.charAt(col + 5) == ' ' || line.charAt(col + 5) == '~' || line.charAt(col + 5) == '(' || line.charAt(col + 5) == ')' || line.charAt(col + 5) == '[' || line.charAt(col + 5) == ']' || line.charAt(col + 5) == '{' || line.charAt(col + 5) == '}' || col + 5 == line.length() - 1) {
                            tokens += "BEGIN " + row + ":" + (col + 1) + "\n";
                            col = col + 5;
                        }
                    } else {
                        int currentcol = col;
                        String identifier = "" + line.charAt(col);
                        boolean isIdentifier = true;
                        currentcol++;
                        while ((line.length() - 1) >= currentcol && line.charAt(currentcol) != ' ' && line.charAt(currentcol) != '~' && line.charAt(currentcol) != '(' && line.charAt(currentcol) != ')' && line.charAt(currentcol) != '{' && line.charAt(currentcol) != '}' && line.charAt(currentcol) != '[' && line.charAt(currentcol) != ']') {
                            if ((line.charAt(currentcol) >= 'a' && line.charAt(currentcol) <= 'z') || (line.charAt(currentcol) >= '0' && line.charAt(currentcol) <= '9') || (line.charAt(currentcol) == '.' || (line.charAt(currentcol) == '+' || (line.charAt(currentcol) == '-')))) {
                                identifier += line.charAt(currentcol);
                                currentcol++;
                            } else {
                                // states
                                ArrayList<Integer> states = new ArrayList<Integer>();
                                if (line.indexOf("{", currentcol) != -1)
                                    states.add(line.indexOf("{", currentcol));
                                if (line.indexOf("}", currentcol) != -1)
                                    states.add(line.indexOf("}", currentcol));
                                if (line.indexOf("(", currentcol) != -1)
                                    states.add(line.indexOf("(", currentcol));
                                if (line.indexOf(")", currentcol) != -1)
                                    states.add(line.indexOf(")", currentcol));
                                if (line.indexOf("[", currentcol) != -1)
                                    states.add(line.indexOf("[", currentcol));
                                if (line.indexOf("]", currentcol) != -1)
                                    states.add(line.indexOf("]", currentcol));
                                if (line.indexOf(" ", currentcol) != -1)
                                    states.add(line.indexOf(" ", currentcol));
                                if (line.indexOf("~", currentcol) != -1)
                                    states.add(line.indexOf("~", currentcol));

                                states.add(line.length());

                                int min = Collections.min(states);
                                String edited = line.substring(currentcol, min);
                                identifier += edited;
                                System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + identifier + "'");
                                printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + identifier + "'");
                                printer.close();
                                System.exit(1);
                                isIdentifier = false;
                                col += identifier.length();
                                break;
                            }
                        }
                        if (isIdentifier) {
                            tokens += "IDENTIFIER " + row + ":" + (col + 1) + "\n";
                            col += identifier.length();
                        }
                    }

                // Detect characters
                } else if (currentCh == '\'') {
                    String chars; // variable to keep char
                    int currentcol = col + 1;
                    boolean isAChar = false;

                    // Figure out where the character ends
                    while(line.length() - 1 >= currentcol){
                        if(line.indexOf('\'', col + 1) == -1)
                        // If there is no second apostrophe after the first one, break the loop.
                            break;
                        if(line.charAt(currentcol) == '\\'){
                        // If character is \', this can not end the character so increment currentcol by not 1 but 2.
                            currentcol += 2;
                        }
                        else if(line.charAt(currentcol) == '\''){
                        // if apostrophe for end of the character is found, break the loop.
                            isAChar = true;
                            break;
                        }
                        else
                            currentcol++;
                    }

                    if(!isAChar){ // If it is not a string give an error and exit the system.
                        chars = line.substring(col);
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + chars + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + chars + "'");
                        printer.close();
                        System.exit(1);
                    }
                    chars = line.substring(col, currentcol+1);
                    if (chars.length() == 3 && chars.charAt(1) != '\\' && chars.charAt(1) != '\'' && chars.charAt(2) == '\'') {
                        tokens += "CHAR " + row + ":" + (col + 1) + "\n";
                        col += 3;
                    } else if (chars.length() == 4 && chars.charAt(3) == '\'' && chars.charAt(1) == '\\' && (chars.charAt(2) == '\\' || chars.charAt(2) == '\'')) {
                        tokens += "CHAR " + row + ":" + (col + 1) + "\n";
                        col += 4;
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + chars + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + chars + "'");
                        printer.close();
                        System.exit(1);
                    }

                // Detect strings
                } else if (currentCh == '"') {
                    String str; // variable to keep string
                    int currentcol = col + 1;
                    boolean isAString = false;

                    // Figure out where the string ends
                    while(line.length() - 1 >= currentcol){
                        if(line.indexOf('"', col + 1) == -1)
                        // If there is no second quotation mark after the first one, break the loop.
                            break;
                        if(line.charAt(currentcol) == '\\'){
                        // If character is \", this can not end the string so increment currentcol by not 1 but 2.
                            currentcol += 2;
                        }
                        else if(line.charAt(currentcol) == '"'){
                        // if quotation mark for end of the string is found, break the loop.
                            isAString = true;
                            break;
                        }
                        else
                            currentcol++;
                    }

                    if(!isAString){ // If it is not a string give an error and exit the system.
                        str = line.substring(col);
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                        printer.close();
                        System.exit(1);
                    }
                    str = line.substring(col, currentcol+1);
                    if (str.length() > 2 && str.charAt(str.length() - 1) == '"' && !str.contains("\\")) {
                        tokens += "STRING " + row + ":" + (col + 1) + "\n";
                        col += str.length();
                    } else if (str.length() > 2 && str.charAt(str.length() - 1) == '"' && str.contains("\\")) {
                        int count = 0;
                        boolean isValidString = true;
                        while (count != str.length() - 3) {
                            if (str.charAt(count) == '\\' && (str.charAt(count + 1) == '\\' || str.charAt(count + 1) == '\"')) {
                                count += 2;
                            } else if (str.charAt(count) != '\\') {
                                count++;
                            } else {
                                isValidString = false;
                                System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                                printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                                printer.close();
                                System.exit(1);
                            }
                        }
                        if (isValidString) {
                            tokens += "STRING " + row + ":" + (col + 1) + "\n";
                            col += str.length();
                        }
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + str + "'");
                        printer.close();
                        System.exit(1);
                    }
                } else if ((currentCh <= '9' && currentCh >= '0') || currentCh == '+' || currentCh == '-' || currentCh == '.') {
                    isNumber(line, currentCh);
                } else {
                    System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Undefined token");
                    printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Undefined token");
                    printer.close();
                    System.exit(1);
                }
            }
            row++;
        }
        System.out.println(tokens);
        printer.print(tokens);
        printer.close();
    }

    public static void isNumber(String line, char currentCh) {
        String number = "" + currentCh;
        int currentcol = col + 1;
        while ((line.length() - 1) >= currentcol && (line.charAt(currentcol) != '~' && line.charAt(currentcol) != ' ' && line.charAt(currentcol) != '('
                && line.charAt(currentcol) != ')' && line.charAt(currentcol) != '{' && line.charAt(currentcol) != '}' && line.charAt(col + 1) != '[' && line.charAt(col + 1) != ']')) {
            if ((line.length() - 1) >= currentcol) {
                number += line.charAt(currentcol);
                currentcol++;
            } else {
                break;
            }
        }
        if (currentCh == '0' && line.charAt(col + 1) == 'b') {
            boolean isBinary = true;
            int counterCol = col + 2;
            while (true) {
                if ((number.length() - 1) >= col && (line.charAt(counterCol) == '0' || line.charAt(counterCol) == '1')) {
                    counterCol++;
                    if (number.length() == counterCol) {
                        break;
                    }
                } else {
                    isBinary = false;
                    System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                    printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                    printer.close();
                    System.exit(1);
                    break;
                }
            }
            if (isBinary) {
                tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                col += number.length();
            }
        } else if (currentCh == '0' && line.charAt(col + 1) == 'x') {
            boolean isHex = true;
            int counterCol = col + 2;
            while (true) {
                if ((number.length() - 1) >= col && ((line.charAt(counterCol) <= '9' && line.charAt(counterCol) >= '0') || (line.charAt(counterCol) <= 'F' && line.charAt(counterCol) >= 'A') || (line.charAt(counterCol) <= 'f' && line.charAt(counterCol) >= 'a'))) {
                    counterCol++;
                    if (number.length() == counterCol) {
                        break;
                    }
                } else {
                    isHex = false;
                    System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                    printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                    printer.close();
                    System.exit(1);
                    break;
                }
            }
            if (isHex) {
                tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                col += number.length();
            }
        } else {
            if (number.contains(".")) {
                if (number.charAt(0) == '+' || number.charAt(0) == '-' || (number.charAt(0) >= '0' && number.charAt(0) <= '9')) {
                    int currentIndex = 1;
                    while (true) {
                        if (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                            currentIndex++;
                        } else if (number.charAt(currentIndex) != '.') {
                            System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                            printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                            printer.close();
                            System.exit(1);
                        } else {
                            break;
                        }
                    }
                    currentIndex++;
                    if ((number.length() - 1) >= currentIndex && number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                        currentIndex++;
                        while (true) {
                            if ((number.length() - 1) >= currentIndex && number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                                currentIndex++;
                            } else {
                                break;
                            }
                        }
                    }
                    if ((number.length() - 1) >= currentIndex && (number.charAt(currentIndex) == 'E' || number.charAt(currentIndex) == 'e')) {
                        currentIndex++;
                        if (number.charAt(currentIndex) == '+' || number.charAt(currentIndex) == '-' || (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9')) {
                            currentIndex++;
                            while (number.length() > currentIndex) {
                                if ((number.length() - 1) >= currentIndex && number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                                    currentIndex++;
                                } else {
                                    System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                    printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                    printer.close();
                                    System.exit(1);
                                }
                            }
                            tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                            col += number.length();
                        }
                    } else if (number.length() == currentIndex) {
                        tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                        col += number.length();
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.close();
                        System.exit(1);
                    }
                }

                if (number.charAt(0) == '.') {
                    int currentIndex = 1;
                    if (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                        currentIndex++;
                        while (true) {
                            if ((number.length() - 1) >= currentIndex && number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                                currentIndex++;
                            } else {
                                break;
                            }
                        }
                    }
                    if ((number.length() - 1) >= currentIndex && (number.charAt(currentIndex) == 'E' || number.charAt(currentIndex) == 'e')) {
                        currentIndex++;
                        if (number.charAt(currentIndex) == '+' || number.charAt(currentIndex) == '-' || (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9')) {

                            if (!(number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') && !(number.charAt(currentIndex + 1) >= '0' && number.charAt(currentIndex + 1) <= '9')) {
                                System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                printer.close();
                                System.exit(1);
                            }
                            while (number.length() > currentIndex) {
                                if ((line.length() - 1) >= currentIndex && number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                                    currentIndex++;
                                } else {
                                    System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                    printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                    printer.close();
                                    System.exit(1);
                                }
                            }
                            tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                            col += number.length();
                        }
                    } else if (number.length() == currentIndex) {
                        tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                        col += number.length();
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.close();
                        System.exit(1);
                    }
                }

            } else if (number.contains("E") || number.contains("e")) {
                int currentIndex = 0;
                if (number.charAt(currentIndex) == '+' || number.charAt(currentIndex) == '-' || (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9')) {
                    if (!(number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') && !(number.charAt(currentIndex + 1) >= '0' && number.charAt(currentIndex + 1) <= '9')) {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.close();
                        System.exit(1);
                    }
                    currentIndex++;
                    while (true) {
                        if (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                            currentIndex++;
                        } else if (number.charAt(currentIndex) == 'E' || number.charAt(currentIndex) == 'e') {
                            currentIndex++;
                            break;
                        } else {
                            System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                            printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                            printer.close();
                            System.exit(1);
                        }
                    }

                    currentIndex++;
                    if (number.charAt(currentIndex) == '+' || number.charAt(currentIndex) == '-' || (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9')) {

                        if (!(number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') && !(number.charAt(currentIndex + 1) >= '0' && number.charAt(currentIndex + 1) <= '9')) {
                            System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                            printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                            printer.close();
                            System.exit(1);
                        }
                        while (number.length() > currentIndex) {
                            if (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                                currentIndex++;
                            } else {
                                System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                                printer.close();
                                System.exit(1);
                            }
                        }
                        tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                        col += number.length();
                    }
                }
            } else if (number.charAt(0) == '+' || number.charAt(0) == '-' || (number.charAt(0) >= '0' && number.charAt(0) <= '9')) {
                int currentIndex = 1;
                if (number.length() > currentIndex && !(number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') && !(number.charAt(currentIndex + 1) >= '0' && number.charAt(currentIndex + 1) <= '9')) {
                    System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                    printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                    printer.close();
                    System.exit(1);
                }
                while (number.length() > currentIndex) {
                    if (number.charAt(currentIndex) >= '0' && number.charAt(currentIndex) <= '9') {
                        if (number.length() != currentIndex + 1)
                            currentIndex++;
                        else
                            break;
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.print("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '" + number + "'");
                        printer.close();
                        System.exit(1);
                    }
                }
                tokens += "NUMBER " + row + ":" + (col + 1) + "\n";
                col += number.length();
            }
        }
    }
}