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
        assertThat(compiled.getClass("org.codingmatters.HelloWorld"), is(aClass()));
        assertThat(compiled.getClass("org.codingmatters.NoSuchClass"), is(not(aClass())));
    }

    @Test
    public void classMatcher_methodNameMatches() throws Exception {
        assertThat(
                compiled.getClass("org.codingmatters.HelloWorld"),
                is(aClass()
                        .with(aMethod().named("sayHello").public_().returning(String.class))
                        .with(aStaticMethod().named("main").public_().withParameters(String[].class).returningVoid())
                )
        );
    }
}
