package me.codvis.ast;

import me.codvis.ast.parser.CPP14BaseListener;
import me.codvis.ast.parser.CPP14Parser;
import me.codvis.ast.parser.CPP14Lexer;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.*;
import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONObject;

/**
 * Class for exstending listeners and parsing code requiered for initial 3D view for code abstraction.
 */
public class CppLstnr_Initial extends CppExtendedListener {
	private FileModel fileModel;

	/**
	 * Constructs the object, setting the filepath for file being parsed.
	 *
	 * @param      filePath  The file path
	 */
	CppLstnr_Initial(String filePath) {
		this.fileModel = new FileModel(filePath);
		this.enterScope(this.fileModel);
	}

	/**
	 * Listener for parsing a method/function declaration. Adding function name to filemodel.
	 *
	 * @param      ctx   The parsing context
	 */
    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        // Get interval between function start and end of function name.
	    Interval interval = new Interval(ctx.start.getStartIndex(), ctx.declarator().stop.getStopIndex());

	    // Get the input stream of function definition rule.
	    CharStream input = ctx.start.getInputStream();

        FunctionModel functionModel = new FunctionModel(input.getText(interval));
       	functionModel.setLineStart(ctx.functionbody().start.getLine());
	    functionModel.setLineEnd(ctx.functionbody().stop.getLine());
	    this.enterScope(functionModel);

    }

    /**
     * Listener for exiting the current scope, expecting that scope to be one entered by enterFunctiondefinition.
     *
     * @param      ctx   The parsing context
     */
    @Override
    public void exitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
    	Model model = this.exitScope();

    	if(model instanceof FunctionModel){
	    	this.scopeStack.peek().addDataInModel((FunctionModel) model);
    	} else {
    		this.enterScope(model);
    		System.err.println("Could not understand parent model for function definition.");
    	}
    }

    /**
	 * Listener for parsing a namespace declaration. Adding namespace to filemodel.
     *
     * @param      ctx   The parsing context
     */
    @Override
	public void enterOriginalnamespacedefinition(CPP14Parser.OriginalnamespacedefinitionContext ctx) {
		NamespaceModel namespace = new NamespaceModel(ctx.Identifier().getText());

	    this.scopeStack.peek().addDataInModel(namespace);
	    this.enterScope(namespace);
	}

    /**
     * Listener for exiting the current scope, expecting that scope to be one entered by enterOriginalnamespacedefinition.
     *
     * @param      ctx   The parsing context
     */
	@Override
	public void exitOriginalnamespacedefinition(CPP14Parser.OriginalnamespacedefinitionContext ctx) {
		this.exitScope();
	}

	/**
	 * Listener for parsing a using namespace declaration. Adding namespace to filemodel.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterUsingdirective(CPP14Parser.UsingdirectiveContext ctx) {
		UsingNamespaceModel usingNamespaceModel;
		if (ctx.nestednamespecifier() != null) {
			usingNamespaceModel = new UsingNamespaceModel(ctx.nestednamespecifier().getText() + ctx.namespacename().getText(), ctx.getStart().getLine());

		} else {
			usingNamespaceModel = new UsingNamespaceModel(ctx.namespacename().getText(), ctx.getStart().getLine());
		}

		this.scopeStack.peek().addDataInModel(usingNamespaceModel);
	}

	/**
	 * Listener for parsing function calls.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterExpressionstatement(CPP14Parser.ExpressionstatementContext ctx) {
	    this.scopeStack.peek().addDataInModel(ctx.getText());
	}
/**

	@Override
	public void enterLiteral(CPP14Parser.LiteralContext ctx) {

		System.out.println("LiteralContext: "+ctx.getText() + " " + ctx.start.getLine());
	}


	@Override
	public void enterPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
		//if(ctx.primaryexpression() != null){
			System.out.println("PostfixexpressionContext: "+ctx.getText() + " " + ctx.start.getLine());
		//}

	}


	@Override
	public void enterMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx) {
			System.out.println("\nMultiplicativeexpressionContext: "+ctx.getText() + " " + ctx.start.getLine());

	}
	@Override
	public void enterAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
		if(ctx.conditionalexpression() != null){
			System.out.println("conditionalexpression: "+ctx.getText() + " " + ctx.start.getLine());
		} else if(ctx.logicalorexpression() != null && isPtrVariable(ctx.initializerclause())){
			System.out.println("\n\nlogicalorexpression: "+ctx.logicalorexpression().getText() + " " + ctx.start.getLine());
			System.out.println("\n\nlogicalorexpression: "+ctx.getText() + " " + ctx.start.getLine());
		} else if(ctx.throwexpression() != null){
			System.out.println("throwexpression: "+ctx.getText() + " " + ctx.start.getLine());
		}
	}
**/

	/**
	 * Listener for adding variable declaration into the scope.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
		System.out.println("SimpledeclarationContext: "+ctx.getText() + " " + ctx.start.getLine());

		if(this.isVariable(ctx)) {
			this.enterScope(new VariableListModel());
		}
	}
/*
	@Override
	public void enterPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
		System.out.println("PostfixexpressionContext: "+ctx.getText() + " " + ctx.start.getLine());
	}

	public boolean isPtrVariable(CPP14Parser.InitializerclauseContext ctx){
		if(ctx.assignmentexpression() != null) {
			return true;
		}

		return false;
	}
*/


	/**
	 * Listener for exiting the current scope, and add data in model parent scope.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void exitSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
		if(this.isVariable(ctx)) {
			VariableListModel variableList = (VariableListModel) this.exitScope();

			if ( this.scopeStack.peek() instanceof FunctionBodyModel ||
			     this.scopeStack.peek() instanceof FileModel ||
			     this.scopeStack.peek() instanceof NamespaceModel) {

				for (Iterator<String> i = variableList.getNames().iterator(); i.hasNext();) {
				    String variableName = i.next();
				    VariableModel vm = new VariableModel(variableName, variableList.getType());
				    vm.trimType();
					this.scopeStack.peek().addDataInModel(vm);
				}

			} else {
	    		System.err.println("Could not understand parent model for simple declaration.");
	    	}
		}
	}

	/**
	 * Determines if ctx is a variable.
	 *
	 * @param      ctx   The parsing context
	 *
	 * @return     True if variable, False otherwise.
	 */
	protected boolean isVariable(CPP14Parser.SimpledeclarationContext ctx){
		CPP14Parser.InitdeclaratorlistContext initDeclList = null;
		CPP14Parser.DeclaratorContext declarator = null;

		if(ctx.initdeclaratorlist() != null) {
			initDeclList = ctx.initdeclaratorlist();
			if(initDeclList.initdeclarator().declarator() != null) {
				declarator = initDeclList.initdeclarator().declarator();
				if(declarator.ptrdeclarator() != null) {
					if(declarator.ptrdeclarator().noptrdeclarator() != null) {
						if(declarator.ptrdeclarator().noptrdeclarator().declaratorid() != null) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Listener for parsing function body.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
		this.enterScope(new FunctionBodyModel());
	}

	/**
	 * Listener for exiting the current scope and adding data into function model scope.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void exitFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
		FunctionBodyModel functionBody = (FunctionBodyModel) this.exitScope();
		this.scopeStack.peek().addDataInModel(functionBody);
	}

	/**
	 * Listener for parsing declaratorid for function model.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterDeclaratorid(CPP14Parser.DeclaratoridContext ctx) {
		Model model = this.exitScope();
		if(model instanceof FunctionModel){
			FunctionModel func = (FunctionModel) model;
			func.setDeclaratorId(ctx.getText());
			this.enterScope(func);
		} else {
			this.enterScope(model);
	    	System.err.println("Could not understand parent model for declarator id. ");
		}
	}

	/**
	 * Listener for parsing scope of a function.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterQualifiedid(CPP14Parser.QualifiedidContext ctx) {
		if(ctx.nestednamespecifier() != null){
			Model model = this.exitScope();
			if(model instanceof FunctionModel) {
				FunctionModel func = (FunctionModel) model;
				func.setScope(ctx.nestednamespecifier().getText());
				this.enterScope(func);
			} else {
				this.enterScope(model);
	    		System.err.println("Could not understand parent model for scope id.");

			}
		}
	}

	/**
	 * Listener for parsing parameters.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
		this.enterScope(new VariableModel());
	}

	/**
	 * Listener for existing the current scope and add data in function model scope.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void exitParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
		VariableModel vm = (VariableModel) this.exitScope();
		Model model = this.exitScope();
		if (model instanceof FunctionModel){
			FunctionModel func = (FunctionModel) model;
			vm.trimType();
			func.addParameter(vm);
			this.enterScope(func);
		} else {
			this.enterScope(model);
	    	System.err.println("Could not understand parent model for parameter declaration.");
		}
	}

	/**
	 * Listener for parsing declarators.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterDeclarator(CPP14Parser.DeclaratorContext ctx) {
		if (this.scopeStack.peek() instanceof VariableModel){
			VariableModel vm = (VariableModel) this.exitScope();
			vm.setName(ctx.getText());
			this.enterScope(vm);
		} else if (this.scopeStack.peek() instanceof VariableListModel) {
			VariableListModel vlm = (VariableListModel) this.exitScope();
			vlm.addName(ctx.getText());
			this.enterScope(vlm);
		} else {
	    	System.err.println("Could not understand parent model for declarator.");
		}
	}

	@Override
	public void enterDeclspecifier(CPP14Parser.DeclspecifierContext ctx) {
		if (this.scopeStack.peek() instanceof VariableModel){
			VariableModel vm = (VariableModel) this.exitScope();
			vm.applyModifierOnType(ctx.getText());
			this.enterScope(vm);
		} else if (this.scopeStack.peek() instanceof VariableListModel) {
			VariableListModel vlm = (VariableListModel) this.exitScope();
			vlm.applyModifierOnType(ctx.getText());
			this.enterScope(vlm);
		} else {
	    	System.err.println("Could not understand parent model for variable modifier.");
		}
	}

	/**
	 * Listener for parsing abstract declarator.
	 *
	 * @param      ctx   The parsing context
	 */
	@Override
	public void enterAbstractdeclarator(CPP14Parser.AbstractdeclaratorContext ctx) {
		if (this.scopeStack.peek() instanceof VariableModel){
			VariableModel vm = (VariableModel) this.exitScope();
			vm.setName(ctx.getText());
			this.enterScope(vm);
		} else {
	    	System.err.println("Could not understand parent model for abstract declarator.");
		}
	}

	/**
	 * Gets the parsed code as JSONObject.
	 *
	 * @return     The parsed code.
	 */
    public JSONObject getParsedCode() {
		JSONObject parsedCode = new JSONObject();

    	return parsedCode.put("file", this.fileModel.getParsedCode());
    }

    /**
     * Gets the file model.
     *
     * @return     The file model.
     */
    public FileModel getFileModel(){
    	return this.fileModel;
    }
 }