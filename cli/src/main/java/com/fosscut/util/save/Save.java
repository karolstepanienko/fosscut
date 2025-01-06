package com.fosscut.util.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import redis.clients.jedis.JedisPooled;

import com.fosscut.type.OrderURI;
import com.fosscut.util.Defaults;
import com.fosscut.util.RedisClient;

public class Save {

    private String cuttingPlan;
    private OrderURI orderUri;
    private File redisConnectionSecretsFile;
    private boolean quietModeRequested;

    public Save(String cuttingPlan, OrderURI orderUri, File redisConnectionSecrets, boolean quietModeRequested) {
        this.cuttingPlan = cuttingPlan;
        this.orderUri = orderUri;
        this.redisConnectionSecretsFile = redisConnectionSecrets;
        this.quietModeRequested = quietModeRequested;
    }

    public void save(File outputFile) {
        if (outputFile == null) {
            if (!quietModeRequested) printIntro();
            printCuttingPlan();
        } else saveCuttingPlanToFile(outputFile);

        if (this.redisConnectionSecretsFile != null) {
            saveCuttingPlanToRedis();
        }
    }

    private void printIntro() {
        System.out.println("");
        System.out.println("Generated cutting plan:");
    }

    private void printCuttingPlan() {
        System.out.println(cuttingPlan);
    }

    private void saveCuttingPlanToFile(File outputFile) {
        try {
            outputFile.createNewFile();  // will do nothing if file exists
            PrintWriter out = new PrintWriter(outputFile);
            out.print(cuttingPlan);
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Unable to save file.");
            System.err.println(e.getMessage());
        }
    }

    private void saveCuttingPlanToRedis() {
        RedisClient redisClient = new RedisClient(redisConnectionSecretsFile);
        JedisPooled jedis = redisClient.getWriteClient();
        if (jedis != null) {
            jedis.set(
                Defaults.REDIS_STRING_KEY_PREFIX
                + Defaults.REDIS_STRING_PLAN_PREFIX
                + orderUri.getIdentifier(),
                cuttingPlan
            );
            jedis.close();
        } else {
            if (!quietModeRequested) System.out.println("Skipping saving plan to redis. Write parameters not found in redis secrets file.");
        }
    }

}
