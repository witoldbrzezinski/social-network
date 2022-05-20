package pl.witoldbrzezinski.SocialNetwork.utils;

import com.auth0.jwt.algorithms.Algorithm;

public class Utils {

    public Algorithm getDefaultJWTAlgorithm(){
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return algorithm;
    }
}
