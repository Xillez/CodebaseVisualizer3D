package me.codvis.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import org.json.JSONObject;
import org.json.JSONArray;

import me.codvis.ast.AccessSpecifierModel;

public class ClassModel extends Model {
	private String name;
	private List<String> parents;
	List<AccessSpecifierModel> accessSpecifiers;

	ClassModel() {
		this.name = "";
		this.accessSpecifiers = new ArrayList<>();
		this.parents = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParents() {
		return this.parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public void addParent(String name) {
		this.parents.add(name);
	}

	public AccessSpecifierModel getAccessSpecifier(String name) {
		// If Access Specifier exists, return it, else nothing.
		for (int i = 0; i < this.accessSpecifiers.size(); i++) {
			if (this.accessSpecifiers.get(i).getName().equals(name)) {
				return this.accessSpecifiers.get(i);
			}
		}
		return null;
	}

	public List<AccessSpecifierModel> getAccessSpecifiers() {
		return this.accessSpecifiers;
	}

	public void setAccessSpecifiers(List<AccessSpecifierModel> data) {
		this.accessSpecifiers = data;
	}

	public void addAccessSpecifier(AccessSpecifierModel data) {
		// If Access Specifier doens't already exist, add it.
		for (AccessSpecifierModel accessSpecifierModel : this.accessSpecifiers) {
			if (accessSpecifierModel.getName().equals(data.getName())) {
				return;
			}
		}

		this.accessSpecifiers.add(data);
	}

	public JSONObject getParsedCode() {
		JSONObject parsedCode = new JSONObject();

		parsedCode.put("name", this.name);

		JSONArray parsedAccessSpecifiers = this.convertClassListJsonObjectList(this.accessSpecifiers);
		if (parsedAccessSpecifiers != null) {
			parsedCode.put("access_specifiers", parsedAccessSpecifiers);
		}

		if (this.parents.size() > 0) {
			parsedCode.put("parents", this.parents);
		}
		return parsedCode;
	}

	/**
	 * Adds the data in model.
	 *
	 * @param data The data
	 */
	@Override
	protected <T> void addDataInModel(T data) {
		if (data instanceof AccessSpecifierModel) {
			this.addAccessSpecifier((AccessSpecifierModel) data);
		} else if (data instanceof String) {
			this.setName((String) data);
		} else {
			System.err.println("Error adding data in class model: " + data.getClass().getName());
			System.exit(1);
		}
	}
}