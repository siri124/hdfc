package hdfc;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.LanguageCapability;
import org.apache.uima.fit.descriptor.ResourceMetaData;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.u_compare.shared.semantic.Place;

/**
 * Describe the analysis engine here - this javadoc will appear as the
 * description within the analysis engine's automatically generated UIMA XML
 * descriptor file.
 * 
 * @author NaCTeM - National Centre of Text Mining
 */
@TypeCapability(inputs = {}, outputs = { "org.u_compare.shared.semantic.Place" }) // Input and output annotation types
@LanguageCapability({ "en" }) // Languages supported by this component
@ResourceMetaData(name="Argo Example Analysis Engine")
public class hdfc extends JCasAnnotator_ImplBase {

	private static final String[] CITIES = { "London", "Paris", "New York", "Shanghai", "Bangkok", "Moscow", "Madrid" };

	/**
	 * An example configuration parameter. For more information on how to use Apache
	 * uimaFIT annotations to define configuration parameters please see
	 * https://goo.gl/XsrA77 [uima.apache.org]. This javadoc will appear as the
	 * description of this parameter within the UIMA XML descriptor.
	 */
	public static final String PARAM_CASE_SENSITIVE = "caseSensitive";
	@ConfigurationParameter(name = PARAM_CASE_SENSITIVE, defaultValue = "true", mandatory = false)
	private boolean caseSensitive;

	/**
	 * Place any initialisation code in this method. This will be performed before
	 * any documents are processed. This method is optional and its definition in
	 * this class can be removed if no initialisation is required.
	 */
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
	}

	/**
	 * This method contains the code required to process documents (represented as
	 * instances of JCas). The example code creates Place annotations for any cities
	 * (from a predefined list) that it recognises in the document text.
	 */
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = jcas.getDocumentText();

		if (!caseSensitive) {
			text = text.toLowerCase();
		}

		for (String city : CITIES) {
			if (!caseSensitive) {
				city = city.toLowerCase();
			}

			int index = text.indexOf(city);
			while (index >= 0) {
				Place place = new Place(jcas);
				place.setBegin(index);
				place.setEnd(index + city.length());
				place.addToIndexes();

				index = text.indexOf(city, place.getEnd());
			}
		}
	}
}