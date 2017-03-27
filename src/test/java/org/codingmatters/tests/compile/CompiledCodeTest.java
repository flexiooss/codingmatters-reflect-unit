package org.codingmatters.tests.compile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;

import static org.codingmatters.tests.reflect.ReflectMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by nelt on 9/6/16.
 */
public class CompiledCodeTest {

    @Rule
    public TemporaryFolder dir = new TemporaryFolder();
    @Rule
    public TemporaryFolder dir2 = new TemporaryFolder();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CompiledCode compiled;

    @Before
    public void setUp() throws Exception {
        File helloWorld = new File(this.dir.newFolder("org", "codingmatters"), "HelloWorld.java");

        try(FileWriter writer = new FileWriter(helloWorld)) {
            writer.write(
                    "package org.codingmatters;\n" +
                    "\n" +
                    "public class HelloWorld {\n" +
                    "    public HelloWorld() {}\n" +
                    "\n" +
                    "    public String sayHello() {return \"Hello, World\";}\n" +
                    "\n" +
                    "    public static void main(String[] args) {\n" +
                    "        System.out.println(new HelloWorld().sayHello());\n" +
                    "    }\n" +
                    "}"
            );
            writer.flush();
        }

        this.compiled = CompiledCode.compile(this.dir.getRoot());
    }

    @Test
    public void compilationFails() throws Exception {
        exception.expect(AssertionError.class);
        exception.expectMessage(is(
                this.dir.getRoot().getAbsolutePath() + "/org/codingmatters/BrokenHello.java:3: error: reached end of file while parsing\n" +
                "public class BrokenHello {\n" +
                "                          ^\n" +
                "\n" +
                "source files with error :\n" +
                this.dir.getRoot().getAbsolutePath() + "/org/codingmatters/BrokenHello.java : \n" +
                "001   package org.codingmatters;\n" +
                "002   \n" +
                "003   public class BrokenHello {\n"
        ));

        File helloWorld = new File(this.dir.getRoot(), "org/codingmatters/BrokenHello.java");

        try(FileWriter writer = new FileWriter(helloWorld)) {
            writer.write(
                    "package org.codingmatters;\n" +
                    "\n" +
                    "public class BrokenHello {\n"
            );
            writer.flush();
        }

        this.compiled = CompiledCode.compile(this.dir.getRoot());
    }

    @Test
    public void getCompiledClass() throws Exception {
        assertThat(compiled.getClass("org.codingmatters.HelloWorld"), is(anInstance().class_()));
        assertThat(compiled.getClass("org.codingmatters.NoSuchClass"), is(not(anInstance().class_())));
    }

    @Test
    public void classMatcher_methodNameMatches() throws Exception {
        assertThat(
                compiled.getClass("org.codingmatters.HelloWorld"),
                is(anInstance().class_()
                        .with(aPublic().method().named("sayHello").returning(String.class))
                        .with(aStatic().public_().method().named("main").withParameters(String[].class).returningVoid())
                )
        );
    }

    @Test
    public void addCompileTarget() throws Exception {
        File newClass = new File(this.dir2.newFolder("org", "codingmatters"), "NewClass.java");

        try(FileWriter writer = new FileWriter(newClass)) {
            writer.write(
                    "package org.codingmatters;\n" +
                            "\n" +
                            "public class NewClass {\n" +
                            "}"
            );
            writer.flush();
        }

        CompiledCode extented = this.compiled.withCompiled(this.dir2.getRoot());

        assertThat(extented.getClass("org.codingmatters.HelloWorld"), is(anInstance().class_()));
        assertThat(extented.getClass("org.codingmatters.NewClass"), is(anInstance().class_()));

        assertThat(compiled.getClass("org.codingmatters.NewClass"), is(not(anInstance().class_())));
    }

    @Test
    public void invokeWithNullArgument_nullMustBeEnclosedInAnArray() throws Exception {
        StringBuilder o = new StringBuilder();
        this.compiled.on(o).invoke("append", String.class).with(new Object [] {null});

        this.exception.expect(AssertionError.class);
        this.exception.expectMessage("(if you meant to invoke method with one null argument, call with new Object[] {null})");
        this.compiled.on(o).invoke("append", String.class).with(null);
    }
}
