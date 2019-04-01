package me.codvis.ast;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

/**
 * Class for abstracting a code namespace.
 */
public class NamespaceModel extends Model {
	private String name;
	private List<FunctionModel> functions;
	private List<NamespaceModel> namespaces;
	private List<UsingNamespaceModel> usingNamespaces;
	private List<String> calls;
	private List<VariableModel> variables;

	/**
	 * Constructs the namespace, setting the name.
	 *
	 * @param name The name
	 */
	public NamespaceModel(String name) {
		this.name = name;
		this.functions = new ArrayList<>();
		this.namespaces = new ArrayList<>();
		this.usingNamespaces = new ArrayList<>();
		this.calls = new ArrayList<>();
		this.variables = new ArrayList<>();
	}

	/**
	 * Gets the name.
	 *
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the functions.
	 *
	 * @return The functions.
	 */
	public List<FunctionModel> getFunctions() {
		return this.functions;
	}

	/**
	 * Gets the namespaces.
	 *
	 * @return The namespaces.
	 */
	public List<NamespaceModel> getNamespaces() {
		return this.namespaces;
	}

	/**
	 * Gets the usingNamespaces.
	 *
	 * @return The usingNamespaces.
	 */
	public List<UsingNamespaceModel> getUsingNamespaces() {
		return this.usingNamespaces;
	}

	/**
	 * Gets the calls.
	 *
	 * @return The calls.
	 */
	public List<String> getCalls() {
		return this.calls;
	}

	/**
	 * Gets the variables.
	 *
	 * @return The variables.
	 */
	public List<VariableModel> getVariables() {
		return this.variables;
	}

	/**
	 * Sets the functions.
	 *
	 * @param functions The functions
	 */
	public void setFunctions(List<FunctionModel> functions) {
		this.functions = functions;
	}

	/**
	 * Sets the inner namespaces.
	 *
	 * @param namespaces The namespaces
	 */
	public void setNamespaces(List<NamespaceModel> namespaces) {
		this.namespaces = namespaces;
	}

	/**
	 * Sets the inner using namespaces.
	 *
	 * @param namespaces The namespaces
	 */
	public void setUsingNamespaces(List<UsingNamespaceModel> namespaces) {
		this.usingNamespaces = namespaces;
	}

	/**
	 * Sets the variables.
	 *
	 * @param variables The variables
	 */
	public void setVariables(List<VariableModel> variables) {
		this.variables = variables;
	}

	/**
	 * Adds a function.
	 *
	 * @param function The function
	 */
	public void addFunction(FunctionModel function) {
		this.functions.add(function);
	}

	/**
	 * Adds a namespace.
	 *
	 * @param namespace The namespace
	 */
	public void addNamespace(NamespaceModel namespace) {
		this.namespaces.add(namespace);
	}

	/**
	 * Adds an using namespace.
	 *
	 * @param namespace The namespace
	 */
	public void addUsingNamespace(UsingNamespaceModel namespace) {
		this.usingNamespaces.add(namespace);
	}

	/**
	 * Adds a call.
	 *
	 * @param functionCall The function call
	 */
	public void addCall(String functionCall) {
		this.calls.add(functionCall);
	}

	/**
	 * Adds a variable.
	 *
	 * @param variable The variable
	 */
	public void addVariable(VariableModel variable) {
		this.variables.add(variable);
	}

	/**
	 * Adds the data in model.
	 *
	 * @param data The data
	 */
	@Override
	protected <T> void addDataInModel(T data) {

		if (data instanceof FunctionModel) {
			this.addFunction((FunctionModel) data);

		} else if (data instanceof NamespaceModel) {
			this.addNamespace((NamespaceModel) data);

		} else if (data instanceof UsingNamespaceModel) {
			this.addUsingNamespace((UsingNamespaceModel) data);

		} else if (data instanceof String) {
			this.addCall((String) data);

		} else if (data instanceof VariableModel) {
			this.addVariable((VariableModel) data);

		} else {
			System.err.println("Error adding data in namespace model");
			System.exit(1);
		}
	}

	/**
	 * Gets the parsed code as JSONObject.
	 *
	 * @return The parsed code.
	 */
	@Override
	public JSONObject getParsedCode() {
		JSONObject parsedCode = new JSONObject();

		parsedCode.put("name", this.name);

		List<JSONObject> parsedFunctions = this.convertClassListJsonObjectList(this.functions, "function");
		if (parsedFunctions != null) {
			parsedCode.put("functions", parsedFunctions);
		}

		List<JSONObject> parsedNamespaces = this.convertClassListJsonObjectList(this.namespaces, "namespace");
		if (parsedNamespaces != null) {
			parsedCode.put("namespaces", parsedNamespaces);
		}

		List<JSONObject> parsedUsingNamespaces = this.convertClassListJsonObjectList(this.usingNamespaces, "namespace");
		if (parsedUsingNamespaces != null) {
			parsedCode.put("using_namespaces", parsedUsingNamespaces);
		}

		List<JSONObject> parsedVariables = this.convertClassListJsonObjectList(this.variables, "variable");
		if (parsedVariables != null) {
			parsedCode.put("variables", parsedVariables);
		}

		if (this.calls.size() > 0) {
			parsedCode.put("calls", this.calls);
		}

		return parsedCode;
	}
}