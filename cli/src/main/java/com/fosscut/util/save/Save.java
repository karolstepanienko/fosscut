package com.fosscut.util.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPooled;

import com.fosscut.shared.SharedDefaults;
import com.fosscut.type.OrderURI;
import com.fosscut.util.RedisClient;

public class Save {

    private static final Logger logger = LoggerFactory.getLogger(Save.class);

    private String fileContent;
    private OrderURI orderUri;
    private File redisConnectionSecretsFile;

    public Save(String fileContent) {
        this.fileContent = fileContent;
        this.orderUri = null;
        this.redisConnectionSecretsFile = null;
    }

    public Save(String fileContent, OrderURI orderUri, File redisConnectionSecrets) {
        this.fileContent = fileContent;
        this.orderUri = orderUri;
        this.redisConnectionSecretsFile = redisConnectionSecrets;
    }

    public void save(File outputFile) {
        if (outputFile != null)
            saveCuttingPlanToFile(outputFile);
        else if (this.redisConnectionSecretsFile != null)
            saveCuttingPlanToRedis();
    }

    private void saveCuttingPlanToFile(File outputFile) {
        try {
            outputFile.createNewFile();  // will do nothing if file exists
            PrintWriter out = new PrintWriter(outputFile);
            out.print(fileContent);
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
                SharedDefaults.REDIS_STRING_KEY_PREFIX
                + SharedDefaults.REDIS_STRING_PLAN_PREFIX
                + orderUri.getIdentifier(),
                fileContent
            );
            jedis.close();
        } else {
            logger.info("Skipping saving plan to redis. Write parameters not found in redis secrets file.");
        }
    }

}
