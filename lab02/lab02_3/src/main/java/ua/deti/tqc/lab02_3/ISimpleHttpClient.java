package ua.deti.tqc.lab02_3;

import java.io.IOException;

public interface ISimpleHttpClient {
    public String doHttpGet(String url) throws IOException;
}
