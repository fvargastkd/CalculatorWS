package org.me.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@BindingType(value = SOAPBinding.SOAP11HTTP_BINDING)
public class AccessToken {

    @XmlAttribute(name = "access_token")
    private String accessToken;

    @XmlAttribute(name = "scope")
    private String scope;

    @XmlAttribute(name = "token_type")
    private String tokenType;

    @XmlAttribute(name = "expires_in")
    private String expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

}
