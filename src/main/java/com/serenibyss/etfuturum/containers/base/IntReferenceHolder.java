package com.serenibyss.etfuturum.containers.base;

public abstract class IntReferenceHolder {

    private int prevValue;

    public static IntReferenceHolder shared(final int[] p_221497_0_, final int p_221497_1_) {
        return new IntReferenceHolder() {
            public int get() {
                return p_221497_0_[p_221497_1_];
            }

            public void set(int p_221494_1_) {
                p_221497_0_[p_221497_1_] = p_221494_1_;
            }
        };
    }

    public static IntReferenceHolder standalone() {
        return new IntReferenceHolder() {
            private int value;

            public int get() {
                return this.value;
            }

            public void set(int p_221494_1_) {
                this.value = p_221494_1_;
            }
        };
    }

    public abstract int get();

    public abstract void set(int p_221494_1_);

    public boolean checkAndClearUpdateFlag() {
        int i = this.get();
        boolean flag = i != this.prevValue;
        this.prevValue = i;
        return flag;
    }
}
