// Generated from cidl.g4 by ANTLR 4.2
package antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link cidlParser}.
 */
public interface cidlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link cidlParser#list_subcomponents}.
	 * @param ctx the parse tree
	 */
	void enterList_subcomponents( cidlParser.List_subcomponentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#list_subcomponents}.
	 * @param ctx the parse tree
	 */
	void exitList_subcomponents( cidlParser.List_subcomponentsContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#interface_required}.
	 * @param ctx the parse tree
	 */
	void enterInterface_required( cidlParser.Interface_requiredContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#interface_required}.
	 * @param ctx the parse tree
	 */
	void exitInterface_required( cidlParser.Interface_requiredContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#component_interface}.
	 * @param ctx the parse tree
	 */
	void enterComponent_interface( cidlParser.Component_interfaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#component_interface}.
	 * @param ctx the parse tree
	 */
	void exitComponent_interface( cidlParser.Component_interfaceContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#interface_provided}.
	 * @param ctx the parse tree
	 */
	void enterInterface_provided( cidlParser.Interface_providedContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#interface_provided}.
	 * @param ctx the parse tree
	 */
	void exitInterface_provided( cidlParser.Interface_providedContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#component}.
	 * @param ctx the parse tree
	 */
	void enterComponent( cidlParser.ComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#component}.
	 * @param ctx the parse tree
	 */
	void exitComponent( cidlParser.ComponentContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#pre}.
	 * @param ctx the parse tree
	 */
	void enterPre( cidlParser.PreContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#pre}.
	 * @param ctx the parse tree
	 */
	void exitPre( cidlParser.PreContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#post}.
	 * @param ctx the parse tree
	 */
	void enterPost( cidlParser.PostContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#post}.
	 * @param ctx the parse tree
	 */
	void exitPost( cidlParser.PostContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#method_header}.
	 * @param ctx the parse tree
	 */
	void enterMethod_header( cidlParser.Method_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#method_header}.
	 * @param ctx the parse tree
	 */
	void exitMethod_header( cidlParser.Method_headerContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#assembly}.
	 * @param ctx the parse tree
	 */
	void enterAssembly( cidlParser.AssemblyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#assembly}.
	 * @param ctx the parse tree
	 */
	void exitAssembly( cidlParser.AssemblyContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#information_flow_spec}.
	 * @param ctx the parse tree
	 */
	void enterInformation_flow_spec( cidlParser.Information_flow_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#information_flow_spec}.
	 * @param ctx the parse tree
	 */
	void exitInformation_flow_spec( cidlParser.Information_flow_specContext ctx);

	/**
	 * Enter a parse tree produced by {@link cidlParser#delegation}.
	 * @param ctx the parse tree
	 */
	void enterDelegation( cidlParser.DelegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link cidlParser#delegation}.
	 * @param ctx the parse tree
	 */
	void exitDelegation( cidlParser.DelegationContext ctx);
}