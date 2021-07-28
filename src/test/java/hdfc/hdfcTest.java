package hdfc;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;
import org.u_compare.shared.semantic.Place;

import hdfc.hdfc;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.JCasFactory.createJCas;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;
import static org.apache.uima.fit.util.JCasUtil.select;

/**
 * Sample tests for the example component. Remove these and add new tests.
 */
public class hdfcTest {

	AnalysisEngineDescription component;

	@Before
	public void setup() throws ResourceInitializationException {
		component = createEngineDescription(hdfc.class);
	}

	@Test
	public void noAnnotations() throws UIMAException {
		JCas jcas = runComponent("Some text not containing any recognisable cities.", true);
		assert (select(jcas, Place.class).size() == 0);
	}

	@Test
	public void noAnnotationsCaseSensitive() throws UIMAException {
		JCas jcas = runComponent("london won't produce an annotation as it doesn't begin with a capital letter.", true);
		assert (select(jcas, Place.class).size() == 0);
	}

	@Test
	public void singleAnnotationCaseInsensitive() throws UIMAException {
		JCas jcas = runComponent("london will produce an annotation as the component will ignore the case of the text.",
				false);
		assert (select(jcas, Place.class).size() == 1);
	}

	@Test
	public void singleAnnotationCaseSensitive() throws UIMAException {
		JCas jcas = runComponent("An annotation will be created for New York, although paris will be ignored.", true);
		assert (select(jcas, Place.class).size() == 1);
	}

	private JCas runComponent(String text, boolean caseSensitive) throws UIMAException {
		component.getAnalysisEngineMetaData().getConfigurationParameterSettings()
				.setParameterValue(hdfc.PARAM_CASE_SENSITIVE, caseSensitive);
		JCas jcas = createJCas();
		jcas.setDocumentText(text);
		runPipeline(jcas, component);
		return jcas;
	}
}
