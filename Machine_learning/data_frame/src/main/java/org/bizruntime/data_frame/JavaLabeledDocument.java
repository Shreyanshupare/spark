package org.bizruntime.data_frame;
@SuppressWarnings("serial")
public class JavaLabeledDocument extends JavaDocument {
	 private double label;

	  public JavaLabeledDocument(long id, String text, double label) {
		  super(id, text);
	    this.label = label;
	  }

	  public double getLabel() {
	    return this.label;
	  }

}
