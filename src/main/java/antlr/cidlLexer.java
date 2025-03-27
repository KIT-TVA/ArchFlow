// Generated from cidl.g4 by ANTLR 4.2
package antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class cidlLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, TYPE=9, 
		COMPONENT=10, IMPLEMENTS=11, LPAREN=12, RPAREN=13, SUBCOMPONENTS=14, INTERFACE_STRING=15, 
		NAME=16, METHOD_NAME=17, SECURITY_LEVEL=18, WS=19;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'postcondition:'", "'->'", "'provide'", "'precondition:'", "'assembly:'", 
		"'require'", "'delegate:'", "'.'", "TYPE", "'component'", "'implements'", 
		"'('", "')'", "'subcomponents'", "'interface'", "NAME", "METHOD_NAME", 
		"SECURITY_LEVEL", "WS"
	};
	public static final String[] ruleNames = {
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "TYPE", 
		"COMPONENT", "IMPLEMENTS", "LPAREN", "RPAREN", "SUBCOMPONENTS", "INTERFACE_STRING", 
		"NAME", "METHOD_NAME", "SECURITY_LEVEL", "WS"
	};


	public cidlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "cidl.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\25\u00ef\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00aa\n\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\21\6\21\u00de\n\21\r\21\16\21\u00df\3\22\6\22\u00e3"+
		"\n\22\r\22\16\22\u00e4\3\23\3\23\3\24\6\24\u00ea\n\24\r\24\16\24\u00eb"+
		"\3\24\3\24\2\2\25\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25\3\2\6\3\2c|\5\2..aac|\5\2JJNNU"+
		"U\5\2\13\f\17\17\"\"\u00f9\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\3)\3\2\2\2\58\3\2"+
		"\2\2\7;\3\2\2\2\tC\3\2\2\2\13Q\3\2\2\2\r[\3\2\2\2\17c\3\2\2\2\21m\3\2"+
		"\2\2\23\u00a9\3\2\2\2\25\u00ab\3\2\2\2\27\u00b5\3\2\2\2\31\u00c0\3\2\2"+
		"\2\33\u00c2\3\2\2\2\35\u00c4\3\2\2\2\37\u00d2\3\2\2\2!\u00dd\3\2\2\2#"+
		"\u00e2\3\2\2\2%\u00e6\3\2\2\2\'\u00e9\3\2\2\2)*\7r\2\2*+\7q\2\2+,\7u\2"+
		"\2,-\7v\2\2-.\7e\2\2./\7q\2\2/\60\7p\2\2\60\61\7f\2\2\61\62\7k\2\2\62"+
		"\63\7v\2\2\63\64\7k\2\2\64\65\7q\2\2\65\66\7p\2\2\66\67\7<\2\2\67\4\3"+
		"\2\2\289\7/\2\29:\7@\2\2:\6\3\2\2\2;<\7r\2\2<=\7t\2\2=>\7q\2\2>?\7x\2"+
		"\2?@\7k\2\2@A\7f\2\2AB\7g\2\2B\b\3\2\2\2CD\7r\2\2DE\7t\2\2EF\7g\2\2FG"+
		"\7e\2\2GH\7q\2\2HI\7p\2\2IJ\7f\2\2JK\7k\2\2KL\7v\2\2LM\7k\2\2MN\7q\2\2"+
		"NO\7p\2\2OP\7<\2\2P\n\3\2\2\2QR\7c\2\2RS\7u\2\2ST\7u\2\2TU\7g\2\2UV\7"+
		"o\2\2VW\7d\2\2WX\7n\2\2XY\7{\2\2YZ\7<\2\2Z\f\3\2\2\2[\\\7t\2\2\\]\7g\2"+
		"\2]^\7s\2\2^_\7w\2\2_`\7k\2\2`a\7t\2\2ab\7g\2\2b\16\3\2\2\2cd\7f\2\2d"+
		"e\7g\2\2ef\7n\2\2fg\7g\2\2gh\7i\2\2hi\7c\2\2ij\7v\2\2jk\7g\2\2kl\7<\2"+
		"\2l\20\3\2\2\2mn\7\60\2\2n\22\3\2\2\2op\7x\2\2pq\7q\2\2qr\7k\2\2r\u00aa"+
		"\7f\2\2st\7k\2\2tu\7p\2\2u\u00aa\7v\2\2vw\7d\2\2wx\7q\2\2xy\7q\2\2y\u00aa"+
		"\7n\2\2z{\7d\2\2{|\7q\2\2|}\7q\2\2}~\7n\2\2~\177\7g\2\2\177\u0080\7c\2"+
		"\2\u0080\u00aa\7p\2\2\u0081\u0082\7H\2\2\u0082\u0083\7n\2\2\u0083\u0084"+
		"\7k\2\2\u0084\u0085\7i\2\2\u0085\u0086\7j\2\2\u0086\u0087\7v\2\2\u0087"+
		"\u0088\7Q\2\2\u0088\u0089\7h\2\2\u0089\u008a\7h\2\2\u008a\u008b\7g\2\2"+
		"\u008b\u008c\7t\2\2\u008c\u00aa\7u\2\2\u008d\u008e\7H\2\2\u008e\u008f"+
		"\7n\2\2\u008f\u0090\7k\2\2\u0090\u0091\7i\2\2\u0091\u0092\7j\2\2\u0092"+
		"\u0093\7v\2\2\u0093\u0094\7Q\2\2\u0094\u0095\7h\2\2\u0095\u0096\7h\2\2"+
		"\u0096\u0097\7g\2\2\u0097\u00aa\7t\2\2\u0098\u0099\7T\2\2\u0099\u009a"+
		"\7g\2\2\u009a\u009b\7s\2\2\u009b\u009c\7w\2\2\u009c\u009d\7g\2\2\u009d"+
		"\u009e\7u\2\2\u009e\u00aa\7v\2\2\u009f\u00a0\7E\2\2\u00a0\u00a1\7E\2\2"+
		"\u00a1\u00aa\7F\2\2\u00a2\u00a3\7C\2\2\u00a3\u00a4\7k\2\2\u00a4\u00a5"+
		"\7t\2\2\u00a5\u00a6\7n\2\2\u00a6\u00a7\7k\2\2\u00a7\u00a8\7p\2\2\u00a8"+
		"\u00aa\7g\2\2\u00a9o\3\2\2\2\u00a9s\3\2\2\2\u00a9v\3\2\2\2\u00a9z\3\2"+
		"\2\2\u00a9\u0081\3\2\2\2\u00a9\u008d\3\2\2\2\u00a9\u0098\3\2\2\2\u00a9"+
		"\u009f\3\2\2\2\u00a9\u00a2\3\2\2\2\u00aa\24\3\2\2\2\u00ab\u00ac\7e\2\2"+
		"\u00ac\u00ad\7q\2\2\u00ad\u00ae\7o\2\2\u00ae\u00af\7r\2\2\u00af\u00b0"+
		"\7q\2\2\u00b0\u00b1\7p\2\2\u00b1\u00b2\7g\2\2\u00b2\u00b3\7p\2\2\u00b3"+
		"\u00b4\7v\2\2\u00b4\26\3\2\2\2\u00b5\u00b6\7k\2\2\u00b6\u00b7\7o\2\2\u00b7"+
		"\u00b8\7r\2\2\u00b8\u00b9\7n\2\2\u00b9\u00ba\7g\2\2\u00ba\u00bb\7o\2\2"+
		"\u00bb\u00bc\7g\2\2\u00bc\u00bd\7p\2\2\u00bd\u00be\7v\2\2\u00be\u00bf"+
		"\7u\2\2\u00bf\30\3\2\2\2\u00c0\u00c1\7*\2\2\u00c1\32\3\2\2\2\u00c2\u00c3"+
		"\7+\2\2\u00c3\34\3\2\2\2\u00c4\u00c5\7u\2\2\u00c5\u00c6\7w\2\2\u00c6\u00c7"+
		"\7d\2\2\u00c7\u00c8\7e\2\2\u00c8\u00c9\7q\2\2\u00c9\u00ca\7o\2\2\u00ca"+
		"\u00cb\7r\2\2\u00cb\u00cc\7q\2\2\u00cc\u00cd\7p\2\2\u00cd\u00ce\7g\2\2"+
		"\u00ce\u00cf\7p\2\2\u00cf\u00d0\7v\2\2\u00d0\u00d1\7u\2\2\u00d1\36\3\2"+
		"\2\2\u00d2\u00d3\7k\2\2\u00d3\u00d4\7p\2\2\u00d4\u00d5\7v\2\2\u00d5\u00d6"+
		"\7g\2\2\u00d6\u00d7\7t\2\2\u00d7\u00d8\7h\2\2\u00d8\u00d9\7c\2\2\u00d9"+
		"\u00da\7e\2\2\u00da\u00db\7g\2\2\u00db \3\2\2\2\u00dc\u00de\t\2\2\2\u00dd"+
		"\u00dc\3\2\2\2\u00de\u00df\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2"+
		"\2\2\u00e0\"\3\2\2\2\u00e1\u00e3\t\3\2\2\u00e2\u00e1\3\2\2\2\u00e3\u00e4"+
		"\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5$\3\2\2\2\u00e6"+
		"\u00e7\t\4\2\2\u00e7&\3\2\2\2\u00e8\u00ea\t\5\2\2\u00e9\u00e8\3\2\2\2"+
		"\u00ea\u00eb\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ed"+
		"\3\2\2\2\u00ed\u00ee\b\24\2\2\u00ee(\3\2\2\2\7\2\u00a9\u00df\u00e4\u00eb"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}