package com.company;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessData implements iProcess {

    private final File file;

    public ProcessData(File file) {
        this.file = file;
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Invalid expression: ");
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Invalid expression: ");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    x = switch (func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                } else {
                    throw new RuntimeException("Invalid expression: ");
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    @Override
    public void ProcessArithmetics() {
        TextReader reader = new TextReader(file);
        String info = reader.readInfo();
        Pattern pattern = Pattern.compile(
                "([+-]?\\d+[.,]?\\d*\\s*)(([+\\-*/^]|\\*{2})\\s*(\\(?[+-]?\\d+[.,]?\\d*\\s*)\\)?)+");
        Matcher matcher = pattern.matcher(info);
        String result = matcher.replaceAll(expression-> {
            String cur = expression.group()
                    .replaceAll("\\s*", "")
                    .replaceAll("\\*{2}", "^")
                    .replaceAll(",", ".");
            try {
                return eval(cur) + " ";
            } catch (RuntimeException e) {
                return "{" + e.getMessage() + cur + "}";
            }
        });
        TextWriter writer = new TextWriter(file);
        writer.writeInfo(result);
    }
}



/*
534+2453
153-56
435845*4635
2845/452
55+56+555
45^4
-3-3
-4.5-2.3
4,5-2
5-(6-7)*3
 */