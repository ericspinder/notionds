package com.notionds.dataSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

public class EvictByLowCountMap<K, V extends EvictByLowCountMap.EvictionByLowCountMember> extends LinkedHashMap<K, V> {

    public interface EvictionByLowCountMember {
        long getCount();
    }
    private int maxSize;
    private int reaperLine = 1;
    private long overflowCount = 0;
    private int unresolvedNesting = 0;
    private final Lock gate = new StampedLock().asWriteLock();

    public EvictByLowCountMap(int max_size) {
        super(max_size);
        maxSize = max_size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > maxSize) {
            try {
                gate.lock();
                if (eldest.getValue().getCount() > reaperLine) {
                    if (size() > ++unresolvedNesting) {
                        reaperLine++;
                        unresolvedNesting = 0;
                    }
                    this.remove(eldest.getKey());
                    this.put(eldest.getKey(), eldest.getValue());
                } else {
                    //can remove
                    overflowCount++;
                    return true;
                }
            }
            finally {
                gate.unlock();
            }
        }
        return false;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getReaperLine() {
        return reaperLine;
    }

    public void setReaperLine(int reaperLine) {
        this.reaperLine = reaperLine;
    }
}
