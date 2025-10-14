package com.fosscut.util.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPooled;

import com.fosscut.shared.SharedDefaults;
import com.fosscut.type.RedisURI;
import com.fosscut.util.RedisClient;
import com.fosscut.util.RedisUriParser;

public class Save {

    private static final Logger logger = LoggerFactory.getLogger(Save.class);

    private SaveContentType contentType;
    private String fileContent;
    private RedisURI orderUri;
    private File redisConnectionSecretsFile;

    public Save(
        SaveContentType contentType,
        String fileContent,
        RedisURI orderUri,
        File redisConnectionSecrets
    ) {
        this.contentType = contentType;
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

    public void save(String outputPath) {
        if (outputPath == null) return;

        if (this.redisConnectionSecretsFile != null && RedisUriParser.isURI(outputPath)) {
            saveCuttingPlanToRedis();
        } else {
            File outputFile = new File(outputPath);
            saveCuttingPlanToFile(outputFile);
        }
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
        String prefix = "";
        if (contentType == SaveContentType.ORDER) prefix = SharedDefaults.REDIS_STRING_ORDER_PREFIX;
        else if (contentType == SaveContentType.PLAN) prefix = SharedDefaults.REDIS_STRING_PLAN_PREFIX;

        RedisClient redisClient = new RedisClient(redisConnectionSecretsFile);
        JedisPooled jedis = redisClient.getWriteClient();

        if (jedis != null) {
            jedis.set(
                SharedDefaults.REDIS_STRING_KEY_PREFIX
                + prefix
                + orderUri.getIdentifier(),
                fileContent
            );
            jedis.close();
        } else {
            logger.info("Skipping saving plan to redis. Write parameters not found in redis secrets file.");
        }
    }

}
