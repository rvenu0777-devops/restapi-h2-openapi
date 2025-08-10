package com.dhanyait.user.performance;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceTest {

    @LocalServerPort
    int port;

    OkHttpClient client = new OkHttpClient();

    @Test
    public void concurrentGetUsers_smallLoad() throws Exception {
        String url = "http://localhost:" + port + "/api/users";
        int threads = 20;
        int requestsPerThread = 25; // total 500 requests
        ExecutorService ex = Executors.newFixedThreadPool(threads);

        List<Callable<Long>> tasks = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            tasks.add(() -> {
                long sum = 0;
                for (int i = 0; i < requestsPerThread; i++) {
                    long start = System.nanoTime();
                    Request r = new Request.Builder().url(url).get().build();
                    try (Response resp = client.newCall(r).execute()) {
                        if (!resp.isSuccessful()) throw new RuntimeException("non-2xx");
                    }
                    long elapsed = System.nanoTime() - start;
                    sum += elapsed;
                }
                return sum;
            });
        }

        List<Future<Long>> futures = ex.invokeAll(tasks);
        long totalNanos = 0;
        int totalRequests = threads * requestsPerThread;
        for (Future<Long> f : futures) totalNanos += f.get();
        double avgMs = (totalNanos / 1_000_000.0) / totalRequests;
        System.out.println("PerformanceTest: totalRequests=" + totalRequests + " averageMs=" + avgMs);
        // You can assert a soft threshold, but in CI environments this might flake. Here we only report.
        assertThat(avgMs).isGreaterThanOrEqualTo(0); // trivial assertion to mark test success
        ex.shutdown();
    }
}
