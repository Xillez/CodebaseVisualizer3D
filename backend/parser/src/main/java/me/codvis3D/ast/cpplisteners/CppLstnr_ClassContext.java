package me.codvis.ast;

import me.codvis.ast.parser.CPP14BaseListener;
import me.codvis.ast.parser.CPP14Parser;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.CharStream;
//import org.antlr.v4.runtime.*;

import org.json.JSONObject;

public class CppLstnr_ClassContext extends CppExtendedListener {
	private FileModel fileModel;

	CppLstnr_ClassContext(String filePath) {
		this.fileModel = new FileModel(filePath);
	}

    @Override 
    public void enterClassspecifier(CPP14Parser.ClassspecifierContext ctx) {   
    	ClassModel classModel;

    	// If it is not a annonymous class.
    	if(ctx.classhead().classheadname() != null){
    		System.out.println("class : " + ctx.classhead().classkey().getText() + " " + ctx.classhead().classheadname().getText() );
    		classModel = new ClassModel(ctx.classhead().classheadname().getText());
    		//fileModel.
    	} else {
    		System.out.println("class : " + ctx.classhead().classkey().getText() + " " +  "null");
    		classModel = new ClassModel("null");
    	}

    	CPP14Parser.AccessspecifierContext currentAccessSpecifier = null;
    	CPP14Parser.MemberspecificationContext currentParseContent = ctx.memberspecification();
    	while(currentParseContent != null){

    		// If member specification is a access specifier (public/protected/private) exists.
    		if(currentParseContent.accessspecifier() != null){

    			currentAccessSpecifier = currentParseContent.accessspecifier();

    		} else {		// Or else it is member declration.

				// Get the interval where the function name in class exists.
   				Interval nameInterval = new Interval(currentParseContent.memberdeclaration().start.getStartIndex(), currentParseContent.memberdeclaration().stop.getStopIndex());
    			
    			// Get the input stream of member declaration context..
    			CharStream input = currentParseContent.memberdeclaration().start.getInputStream();
    			
    			if (currentAccessSpecifier != null) {

		   			System.out.println(currentAccessSpecifier.getText() + ": " + input.getText(nameInterval));
		   			switch(currentAccessSpecifier.getText()){
		   				case "public":
		   					classModel.addPublicData(input.getText(nameInterval));
		   					break;
		   				case "private":
		   					classModel.addPrivateData(input.getText(nameInterval));
		   					break;
		   				case "protected":
		   					classModel.addProtectedData(input.getText(nameInterval));
		   					break;
		   				
		   			}
    		
    			} else { // Or private member declaration exist without private access specifier.
		   	
		   			System.out.println("private : " + input.getText(nameInterval));	
		   			classModel.addPrivateData(input.getText(nameInterval));
  			
	  			}
	    	}

    		// Move to next member specification.
	 		currentParseContent = currentParseContent.memberspecification(); 
    	
    	}

    	fileModel.addClass(classModel);
    }

    public JSONObject getParsedCode() {  
		JSONObject parsedCode = new JSONObject();

    	return parsedCode.put("file", this.fileModel.getParsedCode());
    }
}