package com.ciba.data.gather.manager.uniqueid;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * uniqueId生成链
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public class UniqueIdChain {
    private List<BaseUniqueIdGenerator> mUniqueIdGenerators;
    private Context context;
    private int index;

    public Context getContext() {
        return context;
    }

    public UniqueIdChain(@NonNull Context context, @NonNull List<BaseUniqueIdGenerator> generators, int index) {
        this.mUniqueIdGenerators = generators;
        this.index = index;
        this.context = context;
    }

    public String process() {
        if (index >= mUniqueIdGenerators.size()) {
            return null;
        }
        UniqueIdChain next = new UniqueIdChain(context, mUniqueIdGenerators, index + 1);
        BaseUniqueIdGenerator idGenerator = mUniqueIdGenerators.get(index);
        return idGenerator.getUniqueId(next);
    }

    public boolean isLastNode() {
        return index >= mUniqueIdGenerators.size();
    }
}
