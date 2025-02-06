// Generated from cidl.g4 by ANTLR 4.2
package antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class cidlParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, TYPE=9, 
		COMPONENT=10, IMPLEMENTS=11, LPAREN=12, RPAREN=13, SUBCOMPONENTS=14, INTERFACE_STRING=15, 
		NAME=16, METHOD_NAME=17, SECURITY_LEVEL=18, WS=19, INTERFACE_TYPE=20;
	public static final String[] tokenNames = {
		"<INVALID>", "'postcondition:'", "'->'", "'provide'", "'precondition:'", 
		"'assembly:'", "'require'", "'delegate:'", "'.'", "TYPE", "'component'", 
		"'implements'", "'('", "')'", "'subcomponents'", "'interface'", "NAME", 
		"METHOD_NAME", "SECURITY_LEVEL", "WS", "INTERFACE_TYPE"
	};
	public static final int
		RULE_component = 0, RULE_method_header = 1, RULE_information_flow_spec = 2, 
		RULE_pre = 3, RULE_post = 4, RULE_list_subcomponents = 5, RULE_interface_required = 6, 
		RULE_interface_provided = 7, RULE_assembly = 8, RULE_delegation = 9, RULE_component_interface = 10;
	public static final String[] ruleNames = {
		"component", "method_header", "information_flow_spec", "pre", "post", 
		"list_subcomponents", "interface_required", "interface_provided", "assembly", 
		"delegation", "component_interface"
	};

	@Override
	public String getGrammarFileName() { return "cidl.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public cidlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ComponentContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(cidlParser.NAME, i);
		}
		public List_subcomponentsContext list_subcomponents() {
			return getRuleContext(List_subcomponentsContext.class,0);
		}
		public AssemblyContext assembly(int i) {
			return getRuleContext(AssemblyContext.class,i);
		}
		public List<TerminalNode> NAME() { return getTokens(cidlParser.NAME); }
		public List<DelegationContext> delegation() {
			return getRuleContexts(DelegationContext.class);
		}
		public DelegationContext delegation(int i) {
			return getRuleContext(DelegationContext.class,i);
		}
		public Interface_requiredContext interface_required() {
			return getRuleContext(Interface_requiredContext.class,0);
		}
		public Interface_providedContext interface_provided() {
			return getRuleContext(Interface_providedContext.class,0);
		}
		public TerminalNode COMPONENT() { return getToken(cidlParser.COMPONENT, 0); }
		public TerminalNode IMPLEMENTS() { return getToken(cidlParser.IMPLEMENTS, 0); }
		public List<AssemblyContext> assembly() {
			return getRuleContexts(AssemblyContext.class);
		}
		public ComponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterComponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitComponent(this);
		}
	}

	public final ComponentContext component() throws RecognitionException {
		ComponentContext _localctx = new ComponentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_component);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22); match(COMPONENT);
			setState(23); match(NAME);
			setState(24); match(IMPLEMENTS);
			setState(25); match(NAME);
			setState(26); list_subcomponents();
			setState(27); interface_provided();
			setState(29);
			_la = _input.LA(1);
			if (_la==6) {
				{
				setState(28); interface_required();
				}
			}

			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==5) {
				{
				{
				setState(31); assembly();
				}
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==7) {
				{
				{
				setState(37); delegation();
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Method_headerContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(cidlParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(cidlParser.NAME); }
		public List<TerminalNode> TYPE() { return getTokens(cidlParser.TYPE); }
		public TerminalNode METHOD_NAME() { return getToken(cidlParser.METHOD_NAME, 0); }
		public TerminalNode LPAREN() { return getToken(cidlParser.LPAREN, 0); }
		public Information_flow_specContext information_flow_spec() {
			return getRuleContext(Information_flow_specContext.class,0);
		}
		public TerminalNode TYPE(int i) {
			return getToken(cidlParser.TYPE, i);
		}
		public TerminalNode RPAREN() { return getToken(cidlParser.RPAREN, 0); }
		public Method_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_method_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterMethod_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitMethod_header(this);
		}
	}

	public final Method_headerContext method_header() throws RecognitionException {
		Method_headerContext _localctx = new Method_headerContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_method_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43); information_flow_spec();
			setState(44); match(TYPE);
			setState(45); match(METHOD_NAME);
			setState(46); match(LPAREN);
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TYPE) {
				{
				{
				setState(47); match(TYPE);
				setState(48); match(NAME);
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54); match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Information_flow_specContext extends ParserRuleContext {
		public PreContext pre() {
			return getRuleContext(PreContext.class,0);
		}
		public PostContext post() {
			return getRuleContext(PostContext.class,0);
		}
		public Information_flow_specContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_information_flow_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterInformation_flow_spec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitInformation_flow_spec(this);
		}
	}

	public final Information_flow_specContext information_flow_spec() throws RecognitionException {
		Information_flow_specContext _localctx = new Information_flow_specContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_information_flow_spec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56); pre();
			setState(57); post();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PreContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(cidlParser.NAME, i);
		}
		public TerminalNode SECURITY_LEVEL(int i) {
			return getToken(cidlParser.SECURITY_LEVEL, i);
		}
		public List<TerminalNode> NAME() { return getTokens(cidlParser.NAME); }
		public List<TerminalNode> SECURITY_LEVEL() { return getTokens(cidlParser.SECURITY_LEVEL); }
		public PreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pre; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterPre(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitPre(this);
		}
	}

	public final PreContext pre() throws RecognitionException {
		PreContext _localctx = new PreContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_pre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59); match(4);
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAME) {
				{
				{
				setState(60); match(NAME);
				setState(61); match(SECURITY_LEVEL);
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PostContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(cidlParser.NAME, i);
		}
		public TerminalNode SECURITY_LEVEL(int i) {
			return getToken(cidlParser.SECURITY_LEVEL, i);
		}
		public List<TerminalNode> NAME() { return getTokens(cidlParser.NAME); }
		public List<TerminalNode> SECURITY_LEVEL() { return getTokens(cidlParser.SECURITY_LEVEL); }
		public PostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_post; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterPost(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitPost(this);
		}
	}

	public final PostContext post() throws RecognitionException {
		PostContext _localctx = new PostContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_post);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); match(1);
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAME) {
				{
				{
				setState(68); match(NAME);
				setState(69); match(SECURITY_LEVEL);
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class List_subcomponentsContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(cidlParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(cidlParser.NAME); }
		public List<TerminalNode> COMPONENT() { return getTokens(cidlParser.COMPONENT); }
		public TerminalNode COMPONENT(int i) {
			return getToken(cidlParser.COMPONENT, i);
		}
		public List_subcomponentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list_subcomponents; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterList_subcomponents(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitList_subcomponents(this);
		}
	}

	public final List_subcomponentsContext list_subcomponents() throws RecognitionException {
		List_subcomponentsContext _localctx = new List_subcomponentsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_list_subcomponents);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMPONENT) {
				{
				{
				setState(75); match(COMPONENT);
				setState(76); match(NAME);
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_requiredContext extends ParserRuleContext {
		public List<Method_headerContext> method_header() {
			return getRuleContexts(Method_headerContext.class);
		}
		public Method_headerContext method_header(int i) {
			return getRuleContext(Method_headerContext.class,i);
		}
		public Interface_requiredContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_required; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterInterface_required(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitInterface_required(this);
		}
	}

	public final Interface_requiredContext interface_required() throws RecognitionException {
		Interface_requiredContext _localctx = new Interface_requiredContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_interface_required);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82); match(6);
			setState(84); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(83); method_header();
				}
				}
				setState(86); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==4 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Interface_providedContext extends ParserRuleContext {
		public List<Method_headerContext> method_header() {
			return getRuleContexts(Method_headerContext.class);
		}
		public Method_headerContext method_header(int i) {
			return getRuleContext(Method_headerContext.class,i);
		}
		public Interface_providedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interface_provided; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterInterface_provided(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitInterface_provided(this);
		}
	}

	public final Interface_providedContext interface_provided() throws RecognitionException {
		Interface_providedContext _localctx = new Interface_providedContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_interface_provided);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88); match(3);
			setState(90); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(89); method_header();
				}
				}
				setState(92); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==4 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssemblyContext extends ParserRuleContext {
		public TerminalNode NAME(int i) {
			return getToken(cidlParser.NAME, i);
		}
		public List<TerminalNode> NAME() { return getTokens(cidlParser.NAME); }
		public List<TerminalNode> METHOD_NAME() { return getTokens(cidlParser.METHOD_NAME); }
		public TerminalNode METHOD_NAME(int i) {
			return getToken(cidlParser.METHOD_NAME, i);
		}
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterAssembly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitAssembly(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_assembly);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94); match(5);
			setState(95); match(NAME);
			setState(96); match(8);
			setState(97); match(METHOD_NAME);
			setState(98); match(2);
			setState(99); match(NAME);
			setState(100); match(8);
			setState(101); match(METHOD_NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DelegationContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(cidlParser.NAME, 0); }
		public List<TerminalNode> METHOD_NAME() { return getTokens(cidlParser.METHOD_NAME); }
		public TerminalNode METHOD_NAME(int i) {
			return getToken(cidlParser.METHOD_NAME, i);
		}
		public DelegationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delegation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterDelegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitDelegation(this);
		}
	}

	public final DelegationContext delegation() throws RecognitionException {
		DelegationContext _localctx = new DelegationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_delegation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103); match(7);
			setState(104); match(METHOD_NAME);
			setState(105); match(2);
			setState(106); match(NAME);
			setState(107); match(8);
			setState(108); match(METHOD_NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Component_interfaceContext extends ParserRuleContext {
		public TerminalNode INTERFACE_TYPE() { return getToken(cidlParser.INTERFACE_TYPE, 0); }
		public TerminalNode INTERFACE_STRING() { return getToken(cidlParser.INTERFACE_STRING, 0); }
		public Component_interfaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_component_interface; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).enterComponent_interface(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cidlListener ) ((cidlListener)listener).exitComponent_interface(this);
		}
	}

	public final Component_interfaceContext component_interface() throws RecognitionException {
		Component_interfaceContext _localctx = new Component_interfaceContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_component_interface);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110); match(INTERFACE_TYPE);
			setState(111); match(INTERFACE_STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26t\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2 \n\2\3\2\7\2#\n\2\f\2\16\2&\13"+
		"\2\3\2\7\2)\n\2\f\2\16\2,\13\2\3\3\3\3\3\3\3\3\3\3\3\3\7\3\64\n\3\f\3"+
		"\16\3\67\13\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5A\n\5\f\5\16\5D\13\5"+
		"\3\6\3\6\3\6\7\6I\n\6\f\6\16\6L\13\6\3\7\3\7\7\7P\n\7\f\7\16\7S\13\7\3"+
		"\b\3\b\6\bW\n\b\r\b\16\bX\3\t\3\t\6\t]\n\t\r\t\16\t^\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f"+
		"\2\2\r\2\4\6\b\n\f\16\20\22\24\26\2\2q\2\30\3\2\2\2\4-\3\2\2\2\6:\3\2"+
		"\2\2\b=\3\2\2\2\nE\3\2\2\2\fQ\3\2\2\2\16T\3\2\2\2\20Z\3\2\2\2\22`\3\2"+
		"\2\2\24i\3\2\2\2\26p\3\2\2\2\30\31\7\f\2\2\31\32\7\22\2\2\32\33\7\r\2"+
		"\2\33\34\7\22\2\2\34\35\5\f\7\2\35\37\5\20\t\2\36 \5\16\b\2\37\36\3\2"+
		"\2\2\37 \3\2\2\2 $\3\2\2\2!#\5\22\n\2\"!\3\2\2\2#&\3\2\2\2$\"\3\2\2\2"+
		"$%\3\2\2\2%*\3\2\2\2&$\3\2\2\2\')\5\24\13\2(\'\3\2\2\2),\3\2\2\2*(\3\2"+
		"\2\2*+\3\2\2\2+\3\3\2\2\2,*\3\2\2\2-.\5\6\4\2./\7\13\2\2/\60\7\23\2\2"+
		"\60\65\7\16\2\2\61\62\7\13\2\2\62\64\7\22\2\2\63\61\3\2\2\2\64\67\3\2"+
		"\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67\65\3\2\2\289\7\17\2\2"+
		"9\5\3\2\2\2:;\5\b\5\2;<\5\n\6\2<\7\3\2\2\2=B\7\6\2\2>?\7\22\2\2?A\7\24"+
		"\2\2@>\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\t\3\2\2\2DB\3\2\2\2EJ\7"+
		"\3\2\2FG\7\22\2\2GI\7\24\2\2HF\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2K"+
		"\13\3\2\2\2LJ\3\2\2\2MN\7\f\2\2NP\7\22\2\2OM\3\2\2\2PS\3\2\2\2QO\3\2\2"+
		"\2QR\3\2\2\2R\r\3\2\2\2SQ\3\2\2\2TV\7\b\2\2UW\5\4\3\2VU\3\2\2\2WX\3\2"+
		"\2\2XV\3\2\2\2XY\3\2\2\2Y\17\3\2\2\2Z\\\7\5\2\2[]\5\4\3\2\\[\3\2\2\2]"+
		"^\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_\21\3\2\2\2`a\7\7\2\2ab\7\22\2\2bc\7\n"+
		"\2\2cd\7\23\2\2de\7\4\2\2ef\7\22\2\2fg\7\n\2\2gh\7\23\2\2h\23\3\2\2\2"+
		"ij\7\t\2\2jk\7\23\2\2kl\7\4\2\2lm\7\22\2\2mn\7\n\2\2no\7\23\2\2o\25\3"+
		"\2\2\2pq\7\26\2\2qr\7\21\2\2r\27\3\2\2\2\13\37$*\65BJQX^";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}