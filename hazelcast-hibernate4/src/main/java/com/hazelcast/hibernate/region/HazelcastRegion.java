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

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.hibernate.RegionCache;
import com.hazelcast.logging.ILogger;
import org.hibernate.cache.spi.Region;

/**
 * Hazelcast specific interface version of Hibernate's Region
 *
 * @author Leo Kim (lkim@limewire.com)
 * @param <Cache> implementation type of RegionCache
 */
public interface HazelcastRegion<Cache extends RegionCache> extends Region {

    HazelcastInstance getInstance();

    Cache getCache();

    ILogger getLogger();
}
