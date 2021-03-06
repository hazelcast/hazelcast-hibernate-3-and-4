/*
 * Copyright 2020 Hazelcast Inc.
 *
 * Licensed under the Hazelcast Community License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://hazelcast.com/hazelcast-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.hazelcast.hibernate.region;

import com.hazelcast.hibernate.access.AccessDelegate;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.hibernate.cache.access.SoftLock;

/**
 * Simple adapter implementation for transactional / concurrent access control on entities
 *
 * @author Leo Kim (lkim@limewire.com)
 */
public final class EntityRegionAccessStrategyAdapter implements EntityRegionAccessStrategy {

    private final AccessDelegate<? extends HazelcastEntityRegion> delegate;

    public EntityRegionAccessStrategyAdapter(final AccessDelegate<? extends HazelcastEntityRegion> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean afterInsert(final Object key, final Object value, final Object version) throws CacheException {
        return delegate.afterInsert(key, value, version);
    }

    @Override
    public boolean afterUpdate(final Object key, final Object value, final Object currentVersion,
                               final Object previousVersion, final SoftLock lock) throws CacheException {
        return delegate.afterUpdate(key, value, currentVersion, previousVersion, lock);
    }

    @Override
    public void evict(final Object key) throws CacheException {
        delegate.evict(key);
    }

    @Override
    public void evictAll() throws CacheException {
        delegate.evictAll();
    }

    @Override
    public Object get(final Object key, final long txTimestamp) throws CacheException {
        return delegate.get(key, txTimestamp);
    }

    @Override
    public EntityRegion getRegion() {
        return delegate.getHazelcastRegion();
    }

    @Override
    public boolean insert(final Object key, final Object value, final Object version) throws CacheException {
        return delegate.insert(key, value, version);
    }

    @Override
    public SoftLock lockItem(final Object key, final Object version) throws CacheException {
        return delegate.lockItem(key, version);
    }

    @Override
    public SoftLock lockRegion() throws CacheException {
        return delegate.lockRegion();
    }

    @Override
    public boolean putFromLoad(final Object key, final Object value, final long txTimestamp, final Object version)
            throws CacheException {
        return delegate.putFromLoad(key, value, txTimestamp, version);
    }

    @Override
    public boolean putFromLoad(final Object key, final Object value, final long txTimestamp, final Object version,
                               final boolean minimalPutOverride) throws CacheException {
        return delegate.putFromLoad(key, value, txTimestamp, version, minimalPutOverride);
    }

    @Override
    public void remove(final Object key) throws CacheException {
        delegate.remove(key);
    }

    @Override
    public void removeAll() throws CacheException {
        delegate.removeAll();
    }

    @Override
    public void unlockItem(final Object key, final SoftLock lock) throws CacheException {
        delegate.unlockItem(key, lock);
    }

    @Override
    public void unlockRegion(final SoftLock lock) throws CacheException {
        delegate.unlockRegion(lock);
    }

    @Override
    public boolean update(final Object key, final Object value, final Object currentVersion,
                          final Object previousVersion) throws CacheException {
        return delegate.update(key, value, currentVersion, previousVersion);
    }
}
