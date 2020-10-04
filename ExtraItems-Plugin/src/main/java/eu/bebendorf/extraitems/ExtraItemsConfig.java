package eu.bebendorf.extraitems;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ExtraItemsConfig {

    private SimpleExtraItem[] items = {};
    private transient File file;

    public SimpleExtraItem[] getItems() {
        return items;
    }

    public void setItems(SimpleExtraItem[] items) {
        this.items = items;
    }

    public File getFile() {
        return file;
    }

    public void save() throws IOException {
        if(file == null)
            throw new RuntimeException("This config has no source file set!");
        save(file);
    }

    public void save(File file) throws IOException {
        this.file = file;
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(toJson()).getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
    }

    public static ExtraItemsConfig load(File file) throws IOException {
        if(!file.exists()){
            ExtraItemsConfig config = new ExtraItemsConfig();
            config.save(file);
            return config;
        }
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int r;
        byte[] buffer = new byte[1024];
        while ((r = fis.read(buffer)) != -1)
            baos.write(buffer, 0, r);
        fis.close();
        ExtraItemsConfig config = fromJson(new Gson().fromJson(new String(baos.toByteArray(), StandardCharsets.UTF_8), JsonObject.class));
        config.file = file;
        return config;
    }

    public JsonObject toJson(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().toJsonTree(this).getAsJsonObject();
    }

    public static ExtraItemsConfig fromJson(JsonObject json){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().fromJson(json, ExtraItemsConfig.class);
    }

}
