package com.dy.mcrecorder.mc_recorder.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class InMemoryVectorStore {

    public record EmbeddedDoc(String text, float[] vector){}

    private final List<EmbeddedDoc> docs = new ArrayList<>();

    public void add(String text,float[] vector){
        docs.add(new EmbeddedDoc(text,vector));
    }

    public int size(){
        return docs.size();
    }

    public List<EmbeddedDoc> search(float[] queryVector,int topK){
        /*
        record Scored(EmbeddedDoc doc, float score) {}
        List<Scored> scored = new ArrayList<>();
        for(EmbeddedDoc doc : docs){
            float score = cosineSimilarity(doc.vector(),queryVector);
            scored.add(new Scored(doc,score));
        }
        scored.sort(Comparator.comparingDouble(Scored::score).reversed());
        List<EmbeddedDoc> result = new ArrayList<>();
        int limit = Math.min(topK,scored.size());
        for(int i=0;i<limit;i++){
            result.add(scored.get(i).doc());
        }
        return result;
        */
        record Scored(EmbeddedDoc doc, float score) {}
        return docs.stream()
                .map(doc -> new Scored(doc, cosineSimilarity(doc.vector(), queryVector)))
                .sorted(Comparator.comparingDouble(Scored::score).reversed())
                .limit(topK)
                .map(Scored::doc)
                .toList();
    }

    private static float cosineSimilarity(float[] a, float[] b){
        float dotProduct = 0f;
        float normA = 0f;
        float normB = 0f;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dotProduct / (float) (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
