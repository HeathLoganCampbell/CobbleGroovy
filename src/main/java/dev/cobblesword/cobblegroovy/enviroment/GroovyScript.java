package dev.cobblesword.cobblegroovy.enviroment;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import me.lucko.helper.terminable.composite.CompositeClosingException;
import me.lucko.helper.terminable.composite.CompositeTerminable;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import java.io.IOException;
import java.nio.file.Path;

public class GroovyScript
{
    private Path path;
    private CompositeTerminable compositeTerminable;

    public GroovyScript(Path path)
    {
        this.path = path;
        this.compositeTerminable = CompositeTerminable.create();
    }

    public void eval()
    {

    }

    public void recompile()
    {
        //readBytes from http
    }

    public CompositeTerminable getCompositeTerminable()
    {
        return compositeTerminable;
    }

    public Path getPath()
    {
        return this.path;
    }

    public void run()
    {
        GroovyScriptEngine engine = null;
        try {
            engine = new GroovyScriptEngine(".");
            Binding binding = new Binding();

            binding.setProperty("registry", this.compositeTerminable);

            ImportCustomizer importCustomizer = new ImportCustomizer();

            CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
            compilerConfiguration.addCompilationCustomizers(importCustomizer);

            engine.setConfig(compilerConfiguration);
            System.out.println("Eval " + this.path.toString());
            engine.createScript(this.path.toString(), binding).run();
        } catch (IOException | ResourceException | ScriptException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            this.getCompositeTerminable().close();
        } catch (CompositeClosingException e) {
            e.printStackTrace();
        }
    }
}
