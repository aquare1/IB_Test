package com.aquare.common;

import com.aquare.domain.UserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  令牌工具
 * @author: dengtao
 * createAt: 2019/4/14
 */
@Log4j2
@Component
public class JwtUtils {
	
    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";

    private Map<String, String> tokenMap = new ConcurrentHashMap<>(32);

    public static final String AUTHORIZATION = "Authorization";

    //@Value("${jwt.secret}")
    private String secret = "mySecret";

    //@Value("${jwt.expiration}")
    private Long access_token_expiration = 3600L;

    //@Value("${jwt.refresh}")
    private Long refresh_token_expiration = 7 * 24 * 3600L;


    public Long getUserIDByToken() {
		String token = null;
    	try {
			token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
		}catch(Exception e) {
			//logger.error(e.toString());
		}
    	Long userID = null;
		if(token !=null) {
			userID = getUserIdFromToken(token);
		}
    	return userID;
    }

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public UserDetail getUserFromToken(String token) {
        UserDetail userDetail;
        try {
            final Claims claims = getClaimsFromToken(token);
            String userId = getUserStringIdFromToken(token);
            String username = claims.getSubject();
            String scopeString = claims.get(CLAIM_KEY_AUTHORITIES).toString();
            String[] roleNameList = scopeString.split("|");
            userDetail = new UserDetail();
            userDetail.setId(userId);
            userDetail.setUsername(username);

        } catch (Exception e) {
            userDetail = null;
            log.error("getUserFromToken:",e.toString());
            throw e;
        }
        return userDetail;
    }

    public long getUserIdFromToken(String token) {
        long userId;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = Long.parseLong(String.valueOf(claims.get(CLAIM_KEY_USER_ID)));
        } catch (Exception e) {
            userId = 0;
            log.error("getUserIdFromToken:",e.toString());
            throw e;
        }
        return userId;
    }

    public String getUserStringIdFromToken(String token) {
        String userId= "";
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = String.valueOf(claims.get(CLAIM_KEY_USER_ID));
        } catch (Exception e) {
            log.error("getUserIdFromToken:",e.toString());
            throw e;
        }
        return userId;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            log.error("getUsernameFromToken:",e.toString());
            throw e;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
            log.error("getCreatedDateFromToken:",e.toString());
            throw e;
        }
        return created;
    }

    public String generateAccessToken(UserDetail userDetail) {
        Map<String, Object> claims = generateClaims(userDetail.getId());
        claims.put(CLAIM_KEY_AUTHORITIES, authoritiesToArray(userDetail.getAuthorities()).get(0));
        return generateAccessToken(userDetail.getUsername(), claims);
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
            log.error("getExpirationDateFromToken:",e.toString());
            throw e;
        }
        return expiration;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            refreshedToken = generateAccessToken(claims.getSubject(), claims);

        } catch (Exception e) {
            refreshedToken = null;
            log.error("refreshToken:"+e.toString());
            throw e;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        UserDetail userDetail = (UserDetail) userDetails;
        final String userId = getUserStringIdFromToken(token);
        final String username = getUsernameFromToken(token);
//        final Date created = getCreatedDateFromToken(token);
        return (userId.equals(userDetail.getId())
                && username.equals(userDetail.getUsername())
                && !isTokenExpired(token)
//                && !isCreatedBeforeLastPasswordReset(created, userDetail.getLastPasswordResetDate())
        );
    }

    public String generateRefreshToken(UserDetail userDetail) {
        Map<String, Object> claims = generateClaims(userDetail.getId());
        // 只授于更新 token 的权限
        String roles[] = new String[]{JwtUtils.ROLE_REFRESH_TOKEN};
		claims.put(CLAIM_KEY_AUTHORITIES, JSONChange.objToJson(roles));
        return generateRefreshToken(userDetail.getUsername(), claims);
    }

    public void putToken(String userName, String token) {
        tokenMap.put(userName, token);
    }

    public void deleteToken(String userName) {
        tokenMap.remove(userName);
    }

    public boolean containToken(String userName, String token) {
        if (userName != null && tokenMap.containsKey(userName) && tokenMap.get(userName).equals(token)) {
            return true;
        }
        return false;
    }
    
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
            log.error("getClaimsFromToken:"+e.getMessage());
            throw e;
        }
        return claims;
    }

    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Map<String, Object> generateClaims(Long userId) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USER_ID, userId);
        return claims;
    }

    private Map<String, Object> generateClaims(String userId) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USER_ID, userId);
        return claims;
    }

    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, access_token_expiration);
    }

    private List<String> authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }

    private String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, refresh_token_expiration);
    }

    private String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    private static CompressionCodecResolver codecResolver = new DefaultCompressionCodecResolver();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map parseJwtPayload(String jwt) {
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                CharSequence tokenSeq = io.jsonwebtoken.lang.Strings.clean(sb);
                String token = tokenSeq != null ? tokenSeq.toString() : null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != 2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }
        // =============== Header =================
        Header header = null;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            compressionCodec = codecResolver.resolveCompressionCodec(header);
        }
        // =============== Body =================
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, io.jsonwebtoken.lang.Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }

        Map<String, Object> map = JSONChange.jsonToObj(Map.class,payload);

        return map;
    }

    /* *
     * @Description
     * @Param [val] 从json数据中读取格式化map
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }
}
