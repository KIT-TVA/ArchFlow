package utils;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class AntlErrorListener extends BaseErrorListener {
    private List<String> errorMessages;

    public AntlErrorListener(){
        errorMessages = new ArrayList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errorMessages.add(msg);
    }

    public List<String> getErrorMessages(){
        return errorMessages;
    }

}
