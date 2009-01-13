/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.emitter.EventConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;

/**
 * @see imported from PyYAML
 */
public class PyErrorsTest extends PyImportTest {
    private boolean skip(String filename) {
        List<String> failures = new ArrayList<String>();
        // in python list cannot be a key in a dictionary.
        failures.add("unacceptable-key.loader-error");
        for (String name : failures) {
            if (name.equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public void testLoaderErrors() throws FileNotFoundException {
        File[] files = getStreamsByExtension(".loader-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            if (skip(files[i].getName())) {
                continue;
            }
            try {
                for (Object document : loadAll(new FileInputStream(files[i]))) {
                    assertNotNull("File " + files[i], document);
                }
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // System.err.println("Loading must fail for " +
                // files[i].getAbsolutePath());
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    public void testLoaderStringErrors() throws FileNotFoundException {
        File[] files = getStreamsByExtension(".loader-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            if (skip(files[i].getName())) {
                continue;
            }
            try {
                String content = getResource(files[i].getName());
                for (Object document : loadAll(content.trim())) {
                    assertNotNull(document);
                }
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // System.err.println("Loading must fail for " +
                // files[i].getAbsolutePath());
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    public void testLoaderSingleErrors() throws FileNotFoundException {
        File[] files = getStreamsByExtension(".single-loader-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            try {
                String content = getResource(files[i].getName());
                load(content.trim());
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // multiple documents must not be accepted
                System.err.println("Loading must fail for " + files[i].getAbsolutePath());
            } catch (YAMLException e) {
                assertTrue(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testEmitterErrors() {
        File[] files = getStreamsByExtension(".emitter-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            Constructor constructor = new EventConstructor();
            Loader loader = new Loader(constructor);
            String content = getResource(files[i].getName());
            List<Event> document = (List<Event>) load(loader, content.trim());
            Writer writer = new StringWriter();
            Emitter emitter = new Emitter(writer, new DumperOptions());
            try {
                for (Event event : document) {
                    emitter.emit(event);
                }
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // System.err.println("Loading must fail for " +
                // files[i].getAbsolutePath());
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    // testDumperErrors() is implemented in SerializerTest.java
}
