
package org.me.calculator.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de add complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="add"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="i" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="j" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "add", propOrder = {
    "i",
    "j"
})
public class Add {

    protected int i;
    protected int j;

    /**
     * Obtém o valor da propriedade i.
     * 
     */
    public int getI() {
        return i;
    }

    /**
     * Define o valor da propriedade i.
     * 
     */
    public void setI(int value) {
        this.i = value;
    }

    /**
     * Obtém o valor da propriedade j.
     * 
     */
    public int getJ() {
        return j;
    }

    /**
     * Define o valor da propriedade j.
     * 
     */
    public void setJ(int value) {
        this.j = value;
    }

}
