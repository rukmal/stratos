/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.docker.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.jclouds.docker.domain.Container;
import org.jclouds.docker.domain.Image;
import org.jclouds.json.config.GsonModule;

import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Andrea Turli
 */
public class DockerParserModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(GsonModule.DateAdapter.class).to(GsonModule.Iso8601DateAdapter.class);
   }

   @Provides
   @Singleton
   public Map<Type, Object> provideCustomAdapterBindings() {
      return new ImmutableMap.Builder<Type, Object>()
              .put(Container.class, new ContainerTypeAdapter())
              .put(Image.class, new ImageTypeAdapter())
              .build();
   }

   protected static class ContainerTypeAdapter implements JsonDeserializer<Container> {

      @Override
      public Container deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
              JsonParseException {
         Gson gson = new GsonBuilder().serializeNulls().create();
         final JsonObject jsonObject = json.getAsJsonObject();

         // container:inspect returns ID instead of Id as listContainers
         if (jsonObject.has("ID")) {
            JsonElement id = jsonObject.get("ID");
            jsonObject.remove("ID");
            jsonObject.add("Id", id);
         }
         // container:inspect returns Name instead of Names as listContainers
         if (jsonObject.has("Names")) {
            final JsonArray jsonNamesArray = jsonObject.getAsJsonArray("Names");
            jsonObject.remove("Names");
            jsonObject.add("Name", jsonNamesArray.get(0));
         }
         return gson.fromJson(jsonObject, Container.class);
      }

   }

   protected static class ImageTypeAdapter implements JsonDeserializer<Image> {

      @Override
      public Image deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
              JsonParseException {
         Gson gson = new GsonBuilder().serializeNulls().create();
         final JsonObject jsonObject = json.getAsJsonObject();

         // image:inspect returns id instead of Id as listImages
         if (jsonObject.has("Id")) {
            JsonElement id = jsonObject.get("Id");
            jsonObject.remove("Id");
            jsonObject.add("id", id);
         }
         return gson.fromJson(jsonObject, Image.class);
      }

   }

}
