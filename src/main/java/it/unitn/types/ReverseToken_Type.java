
/* First created by JCasGen Thu Mar 15 21:19:19 CET 2018 */
package it.unitn.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Mar 15 21:19:20 CET 2018
 * @generated */
public class ReverseToken_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = ReverseToken.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("it.unitn.types.ReverseToken");
 
  /** @generated */
  final Feature casFeat_ReversedToken;
  /** @generated */
  final int     casFeatCode_ReversedToken;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getReversedToken(int addr) {
        if (featOkTst && casFeat_ReversedToken == null)
      jcas.throwFeatMissing("ReversedToken", "it.unitn.types.ReverseToken");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ReversedToken);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setReversedToken(int addr, String v) {
        if (featOkTst && casFeat_ReversedToken == null)
      jcas.throwFeatMissing("ReversedToken", "it.unitn.types.ReverseToken");
    ll_cas.ll_setStringValue(addr, casFeatCode_ReversedToken, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public ReverseToken_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_ReversedToken = jcas.getRequiredFeatureDE(casType, "ReversedToken", "uima.cas.String", featOkTst);
    casFeatCode_ReversedToken  = (null == casFeat_ReversedToken) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ReversedToken).getCode();

  }
}



    