package me.codvis.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Method;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import me.codvis.ast.ParserTestCase;
import me.codvis.ast.parser.CPP14Lexer;
import me.codvis.ast.parser.CPP14Parser;
import me.codvis.ast.CppExtendedListener;
import me.codvis.ast.CppLstnr_Initial;

@TestInstance(Lifecycle.PER_CLASS)
public class CppLstnr_InitialTest {
	String filename = "main.cpp";
	private List<ParserTestCase> cases = new ArrayList<>();// Arrays.asList(// ,

	@BeforeAll
	public void setup() {

		ParserTestCase functiondef_basic = new ParserTestCase("functiondefinition", filename, "void test() {}");
		functiondef_basic.expected.add(
				"{\"file\":{\"functions\":[{\"return_type\":\"void\",\"declrator_id\":\"test\",\"name\":\"test()\",\"start_line\":1,\"function_body\":{},\"end_line\":1}],\"file_name\":\""
						+ filename + "\"}}");

		ParserTestCase functiondef_basic_cpp17 = new ParserTestCase("functiondefinition", filename,
				"auto heaven() -> void {}");
		functiondef_basic_cpp17.expected.add(
				"{\"file\":{\"functions\":[{\"return_type\":\"auto\",\"declrator_id\":\"heaven\",\"name\":\"heaven()->void\",\"start_line\":1,\"function_body\":{},\"end_line\":1}],\"file_name\":\""
						+ filename + "\"}}");

		ParserTestCase originalnamespacedefinition = new ParserTestCase("originalnamespacedefinition", filename,
				"namespace Hello {}");
		originalnamespacedefinition.expected.add("{\"file\":{\"file_name\":\"" + filename
				+ "\",\"namespaces\":[{\"name\":\"Hello\"}]}}");

		ParserTestCase usingdirective = new ParserTestCase("usingdirective", filename, "using namespace Hello;");
		usingdirective.expected.add("{\"file\":{\"file_name\":\"" + filename
				+ "\",\"using_namespaces\":[{\"line_number\":1,\"name\":\"Hello\"}]}}");

		ParserTestCase usingdirectiveNested = new ParserTestCase("usingdirective", filename,
				"using namespace Hello::Hi::Bonjour;");
		usingdirectiveNested.expected.add("{\"file\":{\"file_name\":\"" + filename
				+ "\",\"using_namespaces\":[{\"line_number\":1,\"name\":\"Hello::Hi::Bonjour\"}]}}");

		ParserTestCase expressionstatement = new ParserTestCase("expressionstatement", filename, "void test() {f();}");
		expressionstatement.expected.add("{\"file\":{\"file_name\":\"" + filename + "\"}}");

		cases.add(functiondef_basic);
		cases.add(functiondef_basic_cpp17);
		cases.add(originalnamespacedefinition);
		cases.add(usingdirective);
		cases.add(usingdirectiveNested);
		cases.add(expressionstatement);
	}

	@Test
	public void test() {
		for (ParserTestCase caseData : this.cases) {
			System.out.println("---------- Case: " + caseData.listenername + " ----------");

			CPP14Lexer lexer = new CPP14Lexer(new ANTLRInputStream(caseData.input));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			CPP14Parser parser = new CPP14Parser(tokens);

			Method parserMethod = null;
			try {
				parserMethod = parser.getClass().getDeclaredMethod(caseData.listenername);
			} catch (NoSuchMethodException e) {
				fail("The given listener doesn't exsist within ParseTree class");
			}

			Object tree = null;
			try {
				tree = parserMethod.invoke(parser);
			} catch (Exception e) {
				fail("Failed to invoke method: " + e.toString());
			}

			CppExtendedListener listener = new CppLstnr_Initial(caseData.filename);

			ParseTreeWalker walker = new ParseTreeWalker();

			walker.walk(listener, (ParseTree) tree);

			for (String expected : caseData.expected) {
				assertEquals(expected, listener.getParsedCode().toString(), "Not equal");
			}

			for (String illegal : caseData.illegal) {
				assertNotEquals(illegal, listener.getParsedCode().toString(), "Shouldn't be equal");
			}
		}
	}
}