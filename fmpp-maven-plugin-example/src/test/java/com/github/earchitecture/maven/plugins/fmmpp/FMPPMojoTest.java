package com.github.earchitecture.maven.plugins.fmmpp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.Test;

public class FMPPMojoTest extends AbstractMojoTestCase {
	private final File outputDirectory = new File("target/test/generated-sources/fmpp/");
	private final File templateDirectory = new File("src/test/resources/fmpp/templates/");
	private final File cfgFile = new File("src/test/resources/fmpp/");

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testMojoExecution() throws Exception {
		Mojo mojo = getConfiguredMojo();
		mojo.execute();
		assertTrue("Ouptput directory not created.", outputDirectory.exists());
	}

	@Test
	public void testConfiguration() throws Exception {
		Mojo mojo = getConfiguredMojo();
		try {
			setVariableValueToObject(mojo, "cfgFile", null);
			mojo.execute();
		} catch (MojoExecutionException e) {
			assert (e.getMessage().contains("cfgFile"));
		}
	}

	/**
	 * cleans the output directory with all the artifacts created as a result of
	 * mojo execution
	 */
	private void cleanDirectory(final File directory) {
		if (directory != null) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						cleanDirectory(file);
					} else {
						file.delete();
					}
				}
			}
			directory.delete();
		}
	}

	@Test
	public void testCodeGeneration() throws Exception {
		Mojo mojo = getConfiguredMojo();
		mojo.execute();
		assertTrue(outputDirectory.exists());
		List<String> files = Arrays.asList(outputDirectory.list());
		assertTrue(files.size() > 0);
		assertTrue("Output file [AllNumeric.java] not created.", files.contains("applicants.html"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		 cleanDirectory(outputDirectory);
	}

	/**
	 * @return The configured Mojo for testing.
	 * @throws Exception
	 */
	private Mojo getConfiguredMojo() throws Exception {
		File pluginXml = new File(getBasedir(), "src/test/resources/unit/fmpp-mojo/pom.xml");
		Mojo mojo = lookupMojo("generate", pluginXml);
		setVariableValueToObject(mojo, "project", new MavenProjectStub());
		setVariableValueToObject(mojo, "outputDirectory", outputDirectory);
		setVariableValueToObject(mojo, "templateDirectory", templateDirectory);
		setVariableValueToObject(mojo, "cfgFile", cfgFile);
		return mojo;
	}

}
