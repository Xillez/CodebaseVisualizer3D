package me.codvis.ast;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

/**
 * Class for file model.
 */
public class FileModel extends Model{
	private String fileName;
	private List<FunctionModel> functions;
	private List<NamespaceModel> namespaces;
	private List<UsingNamespaceModel> usingNamespaces;

	/**
	 * Constructs the object, setting the filename.
	 *
	 * @param      fileName  The file name
	 */
	FileModel(String fileName){
		this.fileName = fileName;
		this.functions = new ArrayList<>();
		this.namespaces = new ArrayList<>();
		this.usingNamespaces = new ArrayList<>();
	}

	/**
	 * Adds a function.
	 *
	 * @param      function  The function
	 */
	public void addFunction(FunctionModel function){
		this.functions.add(function);
	}

	/**
	 * Adds a namespace.
	 *
	 * @param      namespace  The namespace
	 */
	public void addNamespace(NamespaceModel namespace){
		this.namespaces.add(namespace);
	}

	/**
	 * Adds an using namespace.
	 *
	 * @param      namespace  The namespace
	 */
	public void addUsingNamespace(UsingNamespaceModel namespace){
		this.usingNamespaces.add(namespace);
	}

	/**
	 * Sets the functions.
	 *
	 * @param      functions  The functions
	 */
	public void setFunctions(List<FunctionModel> functions){
		this.functions = functions;
	}

	/**
	 * Gets the filename.
	 *
	 * @return     The filename.
	 */
	public String getFilename(){
		return this.fileName;
	}

	/**
	 * Gets the functions.
	 *
	 * @return     The functions.
	 */
	public List<FunctionModel> getFunctions(){
		return this.functions;
	}


	/**
	 * Gets the namespaces.
	 *
	 * @return     The namespaces.
	 */
	public List<NamespaceModel> getNamespaces(){
		return this.namespaces;
	}

	/**
	 * Gets the using namespaces.
	 *
	 * @return     The used namespaces.
	 */
	public List<UsingNamespaceModel> getUsingNamespaces(){
		return this.usingNamespaces;
	}

	/**
	 * Gets the parsed code as a JSONObject.
	 *
	 * @return     The parsed code.
	 */
	@Override
	public JSONObject getParsedCode(){
		JSONObject parsedCode = new JSONObject();

		parsedCode.put("file_name", this.fileName);

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
		
		return parsedCode;
	}

}