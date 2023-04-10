import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.print("Enter the name of the input file: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        File file = new File(input);
        Scanner scanner = new Scanner(file);
        int row= 1;
        int col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            col = 0;
            while(line.length() > col)
            {
                char currentCh = line.charAt(col);
                if(currentCh == ' '){
                    col++;
                }
                else if(currentCh == '~'){
                    col = 0;
                    break;
                }
                else if(currentCh == '('){
                    col++;
                    System.out.println("LEFTPAR " + row + ":" + col);
                }
                else if(currentCh == ')'){
                    col++;
                    System.out.println("RIGHTPAR " + row + ":" + col);
                }
                else if(currentCh == '['){
                    col++;
                    System.out.println("LEFTSQUAREB " + row + ":" + col);
                }
                else if(currentCh == ']'){
                    col++;
                    System.out.println("RIGHTSQUAREB " + row + ":" + col);
                }
                else if(currentCh == '{'){
                    col++;
                    System.out.println("LEFTCURLYB " + row + ":" + col);
                }
                else if(currentCh == '}'){
                    col++;
                    System.out.println("RIGHTCURLYB " + row + ":" + col);
                }
                else if(currentCh == '.' || currentCh == '+' || currentCh =='-') {
                    if ((line.length() - 1) >= (col + 1) && line.charAt(col + 1) != ' ' && line.charAt(col + 1) != '(' && line.charAt(col + 1) != ')' && line.charAt(col + 1) != '{' && line.charAt(col + 1) != '}' && line.charAt(col + 1) != '[' && line.charAt(col + 1) != ']') {
                        System.out.println("IDENTIFIER " + row + ":" + (col + 1));
                        col++;
                    } else {
                        System.out.println("LEXICAL ERROR [" + row + ":" + (col + 1) + "]: Invalid token '"); // ADD THE WHOLE IDENTIFIER
                    }
                }
                else if( (currentCh >= 'a' && currentCh <= 'z')  ||  currentCh == '!' ||  currentCh == '*'  ||  currentCh == '/' ||  currentCh == ':' ||  currentCh == '<' ||  currentCh == '>' ||  currentCh == '=' ||  currentCh == '?') {
                    if(line.substring(col).length() >= 4 && line.substring(col, col + 4).equals("true") && line.charAt(col + 4) == ' ') {
                        System.out.println("BOOLEAN " + row + ":" + (col+1));
                        col = col + 4;
                    }
                    else if(line.substring(col).length() >= 5 && line.substring(col, col + 5).equals("false") && line.charAt(col + 5) == ' ') {
                        System.out.println("BOOLEAN " + row+ ":" + (col+1));
                        col = col + 5;
                    }
                    else if(line.substring(col).length() >= 6 && line.substring(col, col + 6).equals("define") && line.charAt(col + 6) == ' '){
                        System.out.println("DEFINE " + row + ":" + (col+1));
                        col = col + 6;
                    }
                    else if(line.substring(col).length() >= 3 && line.substring(col, col + 3).equals("let") && line.charAt(col + 3) == ' '){
                        System.out.println("LET " + row + ":" + (col+1));
                        col = col + 3;
                    }
                    else if(line.substring(col).length() >= 4 && line.substring(col, col + 4).equals("cond") && line.charAt(col + 4) == ' '){
                        System.out.println("COND " + row + ":" + (col+1));
                        col = col + 4;
                    }
                    else if(line.substring(col).length() >= 2 && line.substring(col, col + 2).equals("if") && line.charAt(col + 2) == ' '){
                        System.out.println("IF " + row + ":" + (col+1));
                        col = col + 2;
                    }
                    else if(line.substring(col).length() >= 5 && line.substring(col, col + 5).equals("begin") && line.charAt(col + 5) == ' '){
                        System.out.println("BEGIN " + row + ":" + (col+1));
                        col = col + 5;
                    }
                    else{
                        int currentcol = col;
                        String identifier = "" + line.charAt(col);
                        boolean isIdentifier = true;
                        currentcol++;
                        while(  (line.length() - 1) >= currentcol && line.charAt(currentcol) != ' ' && line.charAt(currentcol) != '(' && line.charAt(currentcol) != ')'  && line.charAt(currentcol) != '{' && line.charAt(currentcol) != '}'  && line.charAt(currentcol) != '[' && line.charAt(currentcol) != ']'){
                            if( (line.charAt(currentcol) >= 'a' && line.charAt(currentcol) <= 'z') || (line.charAt(currentcol) >= '0' && line.charAt(currentcol) <= '9') || (line.charAt(currentcol) == '.' || (line.charAt(currentcol) == '+'|| (line.charAt(currentcol) == '-' ))))
                            {
                                identifier += line.charAt(currentcol);
                                currentcol++;
                            }
                            else {
                                // states
                                ArrayList<Integer> states = new ArrayList<Integer>();
                                if(line.indexOf("{", currentcol) != -1)
                                    states.add(line.indexOf("{", currentcol));
                                if(line.indexOf("}", currentcol) != -1)
                                    states.add(line.indexOf("}", currentcol));
                                if(line.indexOf("(", currentcol) != -1)
                                    states.add(line.indexOf("(", currentcol));
                                if(line.indexOf(")", currentcol) != -1)
                                    states.add(line.indexOf(")", currentcol));
                                if(line.indexOf("[", currentcol) != -1)
                                    states.add(line.indexOf("[", currentcol));
                                if(line.indexOf("]", currentcol) != -1)
                                    states.add(line.indexOf("]", currentcol));
                                if(line.indexOf(" ", currentcol) != -1)
                                    states.add(line.indexOf(" ", currentcol));

                                states.add(line.length());

                                int min = Collections.min(states);
                                String edited = line.substring(currentcol,min);
                                identifier += edited;
                                System.out.println("LEXICAL ERROR [" + row + ":" + (col +1) + "]: Invalid token '" + identifier + "'" );
                                isIdentifier = false;
                                col += identifier.length();
                                break;
                            }
                        }
                        if(isIdentifier) {
                            System.out.println("IDENTIFIER " + row + ":" + (col+1));
                            col += identifier.length();
                        }
                    }
                }
                else if(currentCh == '\''){
                    System.out.println("CHAR " + row + ":" + col);
                    if( (line.charAt(col + 1) == '\\' && line.charAt(col + 2) == '\'') || (line.charAt(col + 1) == '\\' && line.charAt(col + 2) == '\\')  )
                        col += 4;
                    else
                        col += 3;
                }
                else if(currentCh == '"'){
                    col++;
                    System.out.println("STRING " + row + ":" + col);
                    while(true){
                        if(line.charAt(col) == '\\' && line.charAt(col + 1) == '"')
                        {
                            col += 2;
                        }
                        else if(line.charAt(col) == '\\' && line.charAt(col + 1) == '\\')
                        {
                            col += 2;
                        }
                        else if(line.charAt(col) == '"'){
                           col++;
                           break;
                        }
                        else
                            col++;
                    }
                }
                else if ((currentCh <= '9' && currentCh >= '0') || currentCh == '+' || currentCh == '-' || currentCh == '.') {

                        if(currentCh == '0' && line.charAt(col+1) == 'b'){
                            System.out.println("NUMBER " + row + ":" + (col+1));
                            col += 2;
                            while(true)
                            {
                                if(line.charAt(col) == 0 || line.charAt(col) == 1)
                                    col++;
                                else
                                    break;
                            }
                        }
                        else if(currentCh == '0' && line.charAt(col+1) == 'x'){
                            System.out.println("NUMBER " + row + ":" + (col+1));
                            col += 2;
                            while(true)
                            {
                                if((line.charAt(col) <= '9' && line.charAt(col) >= '0') || (line.charAt(col) <= 'F' && line.charAt(col) >= 'A') ||  (line.charAt(col) <= 'f' && line.charAt(col) >= 'a'))
                                    col++;
                                else
                                    break;
                            }
                        }
                        else
                        {

                            System.out.println("NUMBER " + row + ":" + (col+1));
                            col ++;

                        }

                }
                //  else if for .5 or + .5

                else {
                    System.out.println("moin");
                    col++;
                }
            }
            row++;
        }
    }
}