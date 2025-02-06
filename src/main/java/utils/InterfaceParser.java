package utils;

import antlr.cidlLexer;
import antlr.cidlParser;
import antlr.cidlParser.ComponentContext;
import org.antlr.v4.runtime.*;
import java.util.List;

public class InterfaceParser {
    private cidlLexer lexer;
    private CommonTokenStream tokenStream;
    private cidlParser parser;
    private ComponentContext context;
    private AntlErrorListener errorListener;

    public InterfaceParser(){
        errorListener = new AntlErrorListener();
    }

    public ComponentContext parse(String spec){
        ANTLRInputStream in = new ANTLRInputStream(spec);
        lexer = new cidlLexer(in);
        tokenStream = new CommonTokenStream(lexer);
        parser = new cidlParser(tokenStream);

        //Error message listener
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        context = parser.component();
        return context;

    }

    public List<String> getErrors(){
        return errorListener.getErrorMessages();
    }
}

