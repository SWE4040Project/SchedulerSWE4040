package org;

public class WebTokens {
	private String jsonWebToken;
	private String xsrfToken;
	public WebTokens(String compactJws, String xsrfToken2) {
		this.setJsonWebToken(compactJws);
		this.setXsrfToken(xsrfToken2);
	}
	public String getJsonWebToken() {
		return jsonWebToken;
	}
	public void setJsonWebToken(String jsonWebToken) {
		this.jsonWebToken = jsonWebToken;
	}
	public String getXsrfToken() {
		return xsrfToken;
	}
	public void setXsrfToken(String xsrfToken) {
		this.xsrfToken = xsrfToken;
	}
	
	
}
