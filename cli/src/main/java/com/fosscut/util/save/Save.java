package com.fosscut.util.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPooled;

import com.fosscut.type.OrderURI;
import com.fosscut.util.Defaults;
import com.fosscut.util.RedisClient;

public class Save {

    private static final Logger logger = LoggerFactory.getLogger(Save.class);

    private String cuttingPlan;
    private OrderURI orderUri;
    private File redisConnectionSecretsFile;

    public Save(String cuttingPlan, OrderURI orderUri, File redisConnectionSecrets) {
        this.cuttingPlan = cuttingPlan;
        this.orderUri = orderUri;
        this.redisConnectionSecretsFile = redisConnectionSecrets;
    }

    public void save(File outputFile) {
        if (outputFile == null) {
            logger.info("");
            logger.info("Generated cutting plan:");
            logger.info(cuttingPlan);
        } else saveCuttingPlanToFile(outputFile);

        if (this.redisConnectionSecretsFile != null) {
            saveCuttingPlanToRedis();
        }
    }

    private void saveCuttingPlanToFile(File outputFile) {
        try {
            outputFile.createNewFile();  // will do nothing if file exists
            PrintWriter out = new PrintWriter(outputFile);
            out.print(cuttingPlan);
            out.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error("Unable to save file.");
            logger.error(e.getMessage());
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
            logger.info("Skipping saving plan to redis. Write parameters not found in redis secrets file.");
        }
    }

}
