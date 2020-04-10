package io.pomelo.util.jwt;

import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;

/**
 * Json Web Token util
 * 
 * @ClassName JwtUtil.java
 * @author PomeloMan
 */
public class JwtUtil {

	public final static SignatureAlgorithm DEFAULT_ALGORITHM = SignatureAlgorithm.HS512;
	public final static String DEFAULT_SECRET = "{{secret}}";
	public final static long DEFAULT_EXPIRATION = 86400000l; // 1day

	private SignatureAlgorithm algorithm = DEFAULT_ALGORITHM;
	private String secret = DEFAULT_SECRET;
	private long expiration = DEFAULT_EXPIRATION;

	public JwtUtil() {
		super();
	}

	public JwtUtil(SignatureAlgorithm algorithm, String secret, long expiration) {
		super();
		this.algorithm = algorithm;
		this.secret = secret;
		this.expiration = expiration;
	}

	public String generateToken(String subject, Map<String, Object> claims) {
		return generateToken(algorithm, subject, claims, expiration, secret);
	}

	public Claims getClaimByToken(String token) {
		return getClaimByToken(token, secret);
	}

	public static String generateToken(SignatureAlgorithm algorithm, String subject, Map<String, Object> claims,
			long expiration, String secret) {
		Date nowDate = new Date();
		Date expireDate = new Date(System.currentTimeMillis() + expiration);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(nowDate)
				.setExpiration(expireDate)
				.signWith(algorithm, Base64.decodeBase64(secret))
				.compact();
	}

	public static Claims getClaimByToken(String token, String secret) {
		Assert.notNull(token);
		Jws<Claims> jws = Jwts.parser().setSigningKey(Base64.decodeBase64(secret)).parseClaimsJws(token);
		return jws.getBody();
	}

	public SignatureAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(SignatureAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

}
