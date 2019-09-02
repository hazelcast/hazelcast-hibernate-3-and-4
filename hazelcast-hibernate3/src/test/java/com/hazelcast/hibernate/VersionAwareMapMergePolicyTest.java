package com.hazelcast.hibernate;

import com.hazelcast.test.HazelcastSerialClassRunner;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.cache.entry.CacheEntry;
import com.hazelcast.spi.merge.MergingValue;
import com.hazelcast.spi.merge.SplitBrainMergePolicy;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HazelcastSerialClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class VersionAwareMapMergePolicyTest {


    private static final MockVersion versionOld = new MockVersion(0);
    private static final MockVersion versionNew = new MockVersion(1);

    protected SplitBrainMergePolicy policy;

    @Before
    public void given() {
        policy = new VersionAwareMapMergePolicy();
    }

    @Test
    public void merge_mergingUptodate() {
        CacheEntry existing = cacheEntryWithVersion(versionOld);
        CacheEntry merging = cacheEntryWithVersion(versionNew);

        MergingValue entryExisting = entryWithGivenValue(existing);
        MergingValue entryMerging = entryWithGivenValue(merging);

        assertEquals(merging, policy.merge(entryMerging, entryExisting));
    }

    @Test
    public void merge_mergingStale() {
        CacheEntry existing = cacheEntryWithVersion(versionNew);
        CacheEntry merging = cacheEntryWithVersion(versionOld);

        MergingValue entryExisting = entryWithGivenValue(existing);
        MergingValue entryMerging = entryWithGivenValue(merging);

        assertEquals(existing, policy.merge(entryMerging, entryExisting));
    }

    @Test
    public void merge_mergingNull() {
        CacheEntry existing = null;
        CacheEntry merging = cacheEntryWithVersion(versionNew);

        MergingValue entryExisting = entryWithGivenValue(existing);
        MergingValue entryMerging = entryWithGivenValue(merging);

        assertEquals(merging, policy.merge(entryMerging, entryExisting));
    }


    private CacheEntry cacheEntryWithVersion(MockVersion mockVersion) {
        return new CacheEntry(new Object[]{}, mock(EntityPersister.class), false, mockVersion, mock(SessionImplementor.class), mock(Object.class));
    }

    private MergingValue entryWithGivenValue(Object value) {
        MergingValue mergingValue= mock(MergingValue.class);
        try {
            when(mergingValue.getValue()).thenReturn(value);
            return mergingValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected static class MockVersion implements Comparable<MockVersion> {

        private int version;

        public MockVersion(int version) {
            this.version = version;
        }

        @Override
        public int compareTo(MockVersion o) {
            return version - o.version;
        }
    }
}
