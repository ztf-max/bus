package com.qt.bus.config;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {

        // ----------------------------------------------------------------
        // 1. 配置连接级参数 (ConnectionConfig)
        //    这里设置 ConnectTimeout (握手超时)
        // ----------------------------------------------------------------
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(30)) // 【关键修改】在这里设置连接超时
                // .setSocketTimeout(Timeout.ofSeconds(10)) // 注意：这是Socket层面的读取超时，通常建议用下面的 ResponseTimeout 代替
                .build();

        // ----------------------------------------------------------------
        // 2. 配置连接池管理器 (ConnectionManager)
        //    将上面的 ConnectionConfig 应用到连接池
        // ----------------------------------------------------------------
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultConnectionConfig(connectionConfig);

        // 这里的 MaxTotal 和 DefaultMaxPerRoute 是连接池大小配置，生产环境建议设置
        connectionManager.setMaxTotal(2000);
        connectionManager.setDefaultMaxPerRoute(200);

        // ----------------------------------------------------------------
        // 3. 配置请求级参数 (RequestConfig)
        //    这里设置 ResponseTimeout (等待数据返回的超时时间)
        // ----------------------------------------------------------------
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(300)) // 读取/响应超时
                .setConnectionRequestTimeout(Timeout.ofSeconds(300)) // 从池中获取连接的等待超时
                .build();

        // ----------------------------------------------------------------
        // 4. 组装 HttpClient
        // ----------------------------------------------------------------
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager) // 注入连接池
                .setDefaultRequestConfig(requestConfig)  // 注入默认请求配置
                .build();

        // ----------------------------------------------------------------
        // 5. 包装为 Spring Factory 并构建 RestClient
        // ----------------------------------------------------------------
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                httpClient);

        return builder
                .requestFactory(factory)
                .build();
    }
}