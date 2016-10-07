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
}
