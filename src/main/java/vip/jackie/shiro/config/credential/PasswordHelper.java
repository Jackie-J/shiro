package vip.jackie.shiro.config.credential;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {

    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public String encrypt(String password, String salt) {
        password = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(salt), hashIterations).toHex();
        return password;
    }

    public static void main(String[] args) {
        String password = new SimpleHash("md5", "admin", ByteSource.Util.bytes("salt"), 2).toHex();
        System.out.println(password);
    }

}
