package me.codvis.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import org.json.JSONObject;

/**
 * Class for abstracting a variable.
 */
public class VariableModel extends Model {
	private String name;
	private String type;

	/**
	 * Constructs the object.
	 */
	VariableModel() {
		this.name = "";
		this.type = "";
	}

	/**
	 * Constructs the object.
	 *
	 * @param name The name
	 * @param type The type
	 */
	VariableModel(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Sets the name.
	 *
	 * @param name The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the type.
	 *
	 * @param type The type
	 */
	public void setType(String type) {
		this.type = type;
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
	 * Gets the type.
	 *
	 * @return The type.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Determines if it has type.
	 *
	 * @return True if has type, False otherwise.
	 */
	public boolean hasType() {
		return ((this.type == "") ? false : true);
	}

	/**
	 * Determines if it has name.
	 *
	 * @return True if has name, False otherwise.
	 */
	public boolean hasName() {
		return ((this.name == "") ? false : true);
	}

	/**
	 * Concatenate type with modifiers.
	 *
	 * @param modifier The modifier
	 */
	public void applyModifierOnType(String modifier) {
		this.type += modifier + " ";
	}

	/**
	 * Concatenate type with primitive type.
	 *
	 * @param type The type
	 */
	public void applyUnnanTypeOnType(String type) {
		this.type += type;
	}

	/**
	 * Remove whitespace from both start and end.
	 */
	public void trimType() {
		this.type = this.type.trim();
	}

	/**
	 * Adds the data in model.
	 *
	 * @param data The data
	 */
	@Override

	protected <T> void addDataInModel(T data){
		System.err.println("Error variable is currently not a scope as scopeStack indicated");
		System.exit(1);
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
		parsedCode.put("type", this.type);

		return parsedCode;
	}
}