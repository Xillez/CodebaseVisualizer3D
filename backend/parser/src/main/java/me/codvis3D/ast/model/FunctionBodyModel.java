package me.codvis.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Class for abstracting a code function.
 */
public class FunctionBodyModel extends Model {
	private List<String> calls;
	private List<VariableModel> variables;

	FunctionBodyModel() {
		this.calls = new ArrayList<>();
		this.variables = new ArrayList<>();
	}

	/**
	 * Adds a call.
	 *
	 * @param call The call
	 */
	private void addCall(String call) {
		this.calls.add(call);
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
	 * Sets the variables.
	 *
	 * @param variables The variables
	 */
	public void setVariables(List<VariableModel> variables) {
		this.variables = variables;
	}

	/**
	 * Adds the data in model.
	 *
	 * @param data The data
	 */
	@Override
	protected <T> void addDataInModel(T data) {

		if (data instanceof String) {
			this.addCall((String) data);

		} else if (data instanceof VariableModel) {
			this.addVariable((VariableModel) data);

		} else {
			System.out.println("Error adding data in function model: " + data.getClass());
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

		parsedCode.put("calls", this.calls);

		JSONArray parsedVariables = this.convertClassListJsonObjectList(this.variables);
		if (parsedVariables != null) {
			parsedCode.put("variables", parsedVariables);
		}

		return parsedCode;
	}
}