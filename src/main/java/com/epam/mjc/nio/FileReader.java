package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class FileReader {

    public Profile getDataFromFile(File file) {
        StringBuilder content = new StringBuilder();
        try (RandomAccessFile file2 = new RandomAccessFile(file, "r")) {
            FileChannel channel = file2.getChannel();
            long fileSize = channel.size();
            //Create a buffer
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            int bytesRead = channel.read(buffer);
            while(bytesRead!=-1){
                buffer.flip();
                while(buffer.hasRemaining()){
                    content.append((char)buffer.get());
                }
                buffer.clear();
                bytesRead=channel.read(buffer);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        Map<String, String> keyValueMap = Arrays.stream(content.toString().split("\n"))
                .map(kv -> kv.split(": "))
                .filter(kvArray -> kvArray.length == 2)
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
        keyValueMap.replaceAll((k,v)->v.replaceAll("\\p{Cntrl}", ""));
        Profile profileFromData = new Profile();
        profileFromData.setAge(Integer.parseInt(keyValueMap.get("Age")));
        profileFromData.setName(keyValueMap.get("Name"));
        profileFromData.setEmail(keyValueMap.get("Email"));
        profileFromData.setPhone(Long.parseLong(keyValueMap.get("Phone")));
        return profileFromData;
    }
}
